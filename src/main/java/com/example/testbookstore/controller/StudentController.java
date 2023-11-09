package com.example.testbookstore.controller;

import com.example.testbookstore.dto.BookDTO;
import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.service.JwtService;
import com.example.testbookstore.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("student")
public class StudentController {
    private final JwtService jwtService;
    private final StudentService studentService;

    @PostMapping("/book")
    ResponseEntity<ResponseModel<String>> addBookToList(@RequestParam("name") String bookName, HttpServletRequest request){
        String studentEmail = jwtService.extractUsernameFromHeader(request.getHeader("Authorization"));
        return new ResponseEntity<>(studentService.addBookToList(bookName, studentEmail), HttpStatus.CREATED);
    }
    @GetMapping("/books")
    ResponseEntity<ResponseModel<List<BookDTO>>> listALlBooks(HttpServletRequest request){
        String studentEmail = jwtService.extractUsernameFromHeader(request.getHeader("Authorization"));
        return new ResponseEntity<>(studentService.listAllBooks(studentEmail), HttpStatus.OK);
    }
    @PostMapping("/subscribe")
    ResponseEntity<ResponseModel<String>> subscribeToAuthor(@RequestParam("author") String authorName,
                                                            HttpServletRequest request){
        String studentEmail = jwtService.extractUsernameFromHeader(request.getHeader("Authorization"));
        return new ResponseEntity<>(studentService.subscribeToAuthor(authorName, studentEmail), HttpStatus.OK);
    }
}
