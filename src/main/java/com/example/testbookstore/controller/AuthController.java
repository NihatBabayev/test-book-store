package com.example.testbookstore.controller;

import com.example.testbookstore.dto.AuthRequest;
import com.example.testbookstore.dto.UserDTO;
import com.example.testbookstore.exception.InvalidOtpCodeException;
import com.example.testbookstore.exception.UserAlreadyExistsException;

import com.example.testbookstore.service.EmailService;
import com.example.testbookstore.service.JwtService;
import com.example.testbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@RequestBody UserDTO userDTO,
                                             @RequestParam("type") String type,
                                             @RequestParam(value = "otp", required = false) String otpCode) {
        String email = userDTO.getEmail();

        // Check if the user with the given email already exists in the database
        if (userService.isUserExists(email)) {
            throw new UserAlreadyExistsException();
        }

        if (otpCode == null) {
            // Generate random 4-digit OTP
            String randomOtp = String.valueOf(new Random().nextInt(9000) + 1000);
            // Store the OTP in Redis with a 1-minute expiration time
            redisTemplate.opsForValue().set(email, randomOtp, 1, TimeUnit.MINUTES);
            // Send OTP to the user with email
            emailService.sendEmail(email, "OTP code", "Your OTP code is \n" + randomOtp + "\nDon't share it with others.");
            // Return a success response to the client
            return ResponseEntity.ok("OTP sent to your email. Check your inbox.");
        } else {
            String storedOtp = (String) redisTemplate.opsForValue().get(email);
            if (storedOtp != null && storedOtp.equals(otpCode)) {
                // If OTP matches save the user and generate a JWT token
                userService.saveUser(userDTO, type);
                String jwtToken = jwtService.generateToken(email);
                // Removing the OTP from Redis after verification
                redisTemplate.delete(email);
                // Returning the JWT token to the client
                return ResponseEntity.ok(jwtToken);
            } else if (storedOtp == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred.");
            } else {
                // If OTP is incorrect or expired returns error to client
                throw new InvalidOtpCodeException();
            }
        }
    }



    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
