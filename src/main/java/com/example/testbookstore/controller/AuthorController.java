package com.example.testbookstore.controller;

import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.entity.Book;
import com.example.testbookstore.service.AuthorService;
import com.example.testbookstore.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final JwtService jwtService;


    @PostMapping("/book")
    ResponseEntity<ResponseModel<String>> createBook(@RequestBody Book book, HttpServletRequest request){
        String authorEmail = jwtService.extractUsernameFromHeader(request.getHeader("Authorization"));
        return new ResponseEntity<>(authorService.createBook(book, authorEmail), HttpStatus.CREATED);
    }

    @DeleteMapping("/book")
    ResponseEntity<ResponseModel<String>> deleteBook(@RequestParam("name") String bookName, HttpServletRequest request){
        String authorEmail = jwtService.extractUsernameFromHeader(request.getHeader("Authorization"));
        return new ResponseEntity<>(authorService.deleteBook(bookName, authorEmail), HttpStatus.OK);
    }
}
