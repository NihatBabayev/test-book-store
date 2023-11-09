package com.example.testbookstore;

import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.entity.Book;
import com.example.testbookstore.exception.BookAlreadyExistsException;
import com.example.testbookstore.repository.AuthorRepository;
import com.example.testbookstore.repository.BookRepository;
import com.example.testbookstore.repository.UserRepository;
import com.example.testbookstore.service.EmailService;
import com.example.testbookstore.service.Impl.AuthorServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceUnitTest {

    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    EmailService emailService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AuthorServiceImpl authorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        authorService = new AuthorServiceImpl(authorRepository, bookRepository, emailService, userRepository);
    }

    @Test
    public void testCreateBookFailureBookAlreadyExists() {
        Book book = new Book();
        book.setName("Test Book");

        when(bookRepository.findByName("Test Book")).thenReturn(new Book());

        assertThrows(BookAlreadyExistsException.class, () -> {
            authorService.createBook(book, "author@gmail.com");
        });

        verify(authorRepository, never()).save(any());
    }

    @Test
    public void testDeleteBookSuccess() {
        ResponseModel<String> response = authorService.deleteBook("Test Book", "author@gmail.com");

        assertEquals("Book deleted successfully", response.getMessage());
        verify(authorRepository, times(1)).deactivateBooksByUserEmail("author@gmail.com");
    }
}