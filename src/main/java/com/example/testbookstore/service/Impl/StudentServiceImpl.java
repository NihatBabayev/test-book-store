package com.example.testbookstore.service.Impl;

import com.example.testbookstore.dto.BookDTO;
import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.entity.Book;
import com.example.testbookstore.entity.Student;
import com.example.testbookstore.exception.BookAlreadyExistsInStudentList;
import com.example.testbookstore.exception.BookNotFoundException;
import com.example.testbookstore.repository.AuthorRepository;
import com.example.testbookstore.repository.BookRepository;
import com.example.testbookstore.repository.StudentRepository;
import com.example.testbookstore.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public ResponseModel<String> addBookToList(String bookName, String studentEmail) {
        Student student = studentRepository.findByUserEmail(studentEmail);
        ResponseModel<String> responseModel = new ResponseModel<>();

        Book existingBook = bookRepository.findByName(bookName);
        if (existingBook == null) {
            throw new BookNotFoundException();
        }

        if (!student.getBooks().contains(existingBook)) {
            student.getBooks().add(existingBook);
            studentRepository.save(student);
            responseModel.setMessage("Book added to the student's list");
            return responseModel;
        } else {
            throw new BookAlreadyExistsInStudentList();
        }
    }

    @Override
    public ResponseModel<List<BookDTO>> listAllBooks(String studentEmail) {
        ResponseModel<List<BookDTO>> responseModel = new ResponseModel<>();

        responseModel.setData(studentRepository.findBooksByStudent(studentEmail).stream()
                .map(book -> new BookDTO(book.getName(), book.getAuthor().getUser().getName()))
                .collect(Collectors.toList()));
        responseModel.setMessage("All books you read");

        return responseModel;
    }

    @Override
    public ResponseModel<String> subscribeToAuthor(String authorName, String studentEmail) {
        studentRepository.subscribeToAuthor(studentRepository.findByUserEmail(studentEmail).getId(), authorRepository.findByUserName(authorName).getId());
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setMessage("Successfully subscribed to author");
        return responseModel;
    }
}
