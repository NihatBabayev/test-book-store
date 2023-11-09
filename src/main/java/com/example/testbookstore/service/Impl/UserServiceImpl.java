package com.example.testbookstore.service.Impl;

import com.example.testbookstore.dto.UserDTO;
import com.example.testbookstore.entity.Author;
import com.example.testbookstore.entity.Student;
import com.example.testbookstore.entity.User;
import com.example.testbookstore.exception.UserAlreadyExistsException;
import com.example.testbookstore.repository.AuthorRepository;
import com.example.testbookstore.repository.StudentRepository;
import com.example.testbookstore.repository.UserRepository;
import com.example.testbookstore.service.EmailService;
import com.example.testbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;



@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Override
    public void saveUser(UserDTO userDTO, String type) {

        if (isUserExists(userDTO.getEmail())) {
            throw new UserAlreadyExistsException();
        } else {
            String studentString = "student";
            String authorString = "author";
            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            if (type.equals(studentString)) {

                user.setRoles("ROLE_STUDENT");
                userRepository.save(user);

                Student student = new Student();
                student.setAge(userDTO.getAge());
                student.setUser(user);
                studentRepository.save(student);

            } else if (type.equals(authorString)) {
                user.setRoles("ROLE_AUTHOR");
                userRepository.save(user);

                Author author = new Author();
                author.setUser(user);
                author.setAge(userDTO.getAge());
                authorRepository.save(author);
            }
            emailService.sendEmail(userDTO.getEmail(), "Successful Registration", "Hey, "+userDTO.getName()+" welcome to  book-store app.");
        }
    }

    @Override
    public boolean isUserExists(String email) {
        if (userRepository.findByEmail(email) != null) {
            return true;
        }
        else
            return false;
    }
}