package com.example.testbookstore.service;

import com.example.testbookstore.dto.BookDTO;
import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {
    void save(Student student);

    ResponseModel<String> addBookToList(String bookName, String studentEmail);

    ResponseModel<List<BookDTO>> listAllBooks(String studentEmail);

    ResponseModel<String> subscribeToAuthor(String authorName, String studentEmail);
}
