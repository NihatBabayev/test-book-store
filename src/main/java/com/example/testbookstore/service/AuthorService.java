package com.example.testbookstore.service;

import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.entity.Book;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public interface AuthorService {


    ResponseModel<String> createBook(Book book, String authorEmail);

    @Transactional
    ResponseModel<String> deleteBook(String bookName, String authorEmail);
}
