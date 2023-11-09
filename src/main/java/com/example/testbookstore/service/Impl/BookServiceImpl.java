package com.example.testbookstore.service.Impl;

import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.dto.StudentDTO;
import com.example.testbookstore.repository.StudentRepository;
import com.example.testbookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final StudentRepository studentRepository;


    @Override
    public ResponseModel<List<StudentDTO>> listAllReaders(String bookName) {
        ResponseModel<List<StudentDTO>> responseModel = new ResponseModel<>();

        responseModel.setData(studentRepository.findStudentsByBookName(bookName).stream()
                .map(user -> new StudentDTO(user.getName(), user.getEmail()))
                .collect(Collectors.toList()));
        responseModel.setMessage("All students that are currently reading this book");

        return responseModel;
    }
}
