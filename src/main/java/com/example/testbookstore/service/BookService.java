package com.example.testbookstore.service;

import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.dto.StudentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    ResponseModel<List<StudentDTO>> listAllReaders(String bookName);
}
