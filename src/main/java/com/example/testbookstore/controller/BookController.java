package com.example.testbookstore.controller;

import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.dto.StudentDTO;
import com.example.testbookstore.service.Impl.BookServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("book")
@RequiredArgsConstructor
public class BookController {
    private final BookServiceImpl bookService;
    @GetMapping
    ResponseEntity<ResponseModel<List<StudentDTO>>> listAllReaders(@RequestParam("name") String bookName){
        return new ResponseEntity<>(bookService.listAllReaders(bookName), HttpStatus.OK);
    }
}
