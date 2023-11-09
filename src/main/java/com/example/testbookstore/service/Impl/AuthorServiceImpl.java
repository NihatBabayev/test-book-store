package com.example.testbookstore.service.Impl;

import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.entity.Author;
import com.example.testbookstore.entity.Book;
import com.example.testbookstore.exception.BookAlreadyExistsException;
import com.example.testbookstore.repository.AuthorRepository;
import com.example.testbookstore.repository.BookRepository;
import com.example.testbookstore.repository.UserRepository;
import com.example.testbookstore.service.AuthorService;
import com.example.testbookstore.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Override
    public ResponseModel<String> createBook(Book book, String authorEmail) {
        if(bookRepository.findByName(book.getName())!=null){
            throw new BookAlreadyExistsException();
        }
        else {
            Author author = authorRepository.findByUserEmail(authorEmail);

            book.setActive(true);
            book.setAuthor(author);
            author.getAuthoredBooks().add(book);
            authorRepository.save(author);
            //Sending email message to subscribed Students:
            List<String> subscribedStudentEmails = authorRepository.findAllSubscribedStudentEmails(authorEmail);
            String subscriptionText = "The author "+userRepository.findByEmail(authorEmail).getName()+" has crated new book recently. "+
                    "That Book is called " + book.getName();
            emailService.sendEmailsAsync(subscribedStudentEmails, "New Book", subscriptionText);

            ResponseModel<String> responseModel = new ResponseModel<>();
            responseModel.setMessage("Book is successfully added");
            return responseModel;
        }
    }

    @Override
    @Transactional
    public ResponseModel<String> deleteBook(String bookName, String authorEmail) {
        authorRepository.deactivateBooksByUserEmail(authorEmail);
        ResponseModel<String> responseModel = new ResponseModel<>();
        responseModel.setMessage("Book deleted successfully");
        return responseModel;
    }
}