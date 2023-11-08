package com.example.testbookstore;

import com.example.testbookstore.dto.BookDTO;
import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.entity.Author;
import com.example.testbookstore.entity.Book;
import com.example.testbookstore.entity.Student;
import com.example.testbookstore.entity.User;
import com.example.testbookstore.exception.BookAlreadyExistsInStudentList;
import com.example.testbookstore.exception.BookNotFoundException;
import com.example.testbookstore.exception.UserNotFoundException;
import com.example.testbookstore.repository.AuthorRepository;
import com.example.testbookstore.repository.BookRepository;
import com.example.testbookstore.repository.StudentRepository;
import com.example.testbookstore.service.Impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddBookToList_Success() {
        // Arrange
        String bookName = "SampleBook";
        String studentEmail = "student@gmail.com";

        Student student = createStudent(studentEmail);
        Book existingBook = createBook(bookName);

        when(studentRepository.findByUserEmail(studentEmail)).thenReturn(student);
        when(bookRepository.findByName(bookName)).thenReturn(existingBook);
        when(studentRepository.save(student)).thenReturn(student);

        // Act
        ResponseModel<String> responseModel = studentService.addBookToList(bookName, studentEmail);

        // Assert
        assertNotNull(responseModel);
        assertEquals("Book added to the student's list", responseModel.getMessage());
    }

    @Test
    void testAddBookToList_BookNotFound() {
        // Arrange
        String bookName = "Fake Book";
        String studentEmail = "student@gmail.com";

        Student student = createStudent(studentEmail);

        when(studentRepository.findByUserEmail(studentEmail)).thenReturn(student);
        when(bookRepository.findByName(bookName)).thenReturn(null);

        // Act and Assert
        assertThrows(BookNotFoundException.class, () -> {
            studentService.addBookToList(bookName, studentEmail);
        });
    }

    @Test
    void testAddBookToList_BookAlreadyExistsInList() {
        // Arrange
        String bookName = "Fake Book";
        String studentEmail = "student@gmail.com";

        Student student = createStudent(studentEmail);
        Book existingBook = createBook(bookName);
        student.setBooks(Collections.singletonList(existingBook));

        when(studentRepository.findByUserEmail(studentEmail)).thenReturn(student);
        when(bookRepository.findByName(bookName)).thenReturn(existingBook);

        // Act and Assert
        assertThrows(BookAlreadyExistsInStudentList.class, () -> {
            studentService.addBookToList(bookName, studentEmail);
        });
    }

    @Test
    void testListAllBooks() {
        // Arrange
        String studentEmail = "student@gmail.com";
        Student student = createStudent(studentEmail);

        // Create author and book objects
        User authorUser1 = new User();
        authorUser1.setName("Author 1");

        User authorUser2 = new User();
        authorUser2.setName("Author 2");

        Author author1 = new Author();
        author1.setUser(authorUser1);

        Author author2 = new Author();
        author2.setUser(authorUser2);

        Book book1 = new Book();
        book1.setName("Book 1");
        book1.setAuthor(author1);

        Book book2 = new Book();
        book2.setName("Book 2");
        book2.setAuthor(author2);

        when(studentRepository.findBooksByStudent(studentEmail)).thenReturn(Arrays.asList(book1, book2));

        // Act
        ResponseModel<List<BookDTO>> responseModel = studentService.listAllBooks(studentEmail);

        // Assert
        assertNotNull(responseModel);
        assertEquals("All books you read", responseModel.getMessage());

        List<BookDTO> bookDTOList = responseModel.getData();
        assertNotNull(bookDTOList);
        assertEquals(2, bookDTOList.size());
        assertEquals("Book 1", bookDTOList.get(0).getName());
        assertEquals("Author 1", bookDTOList.get(0).getAuthorName());
        assertEquals("Book 2", bookDTOList.get(1).getName());
        assertEquals("Author 2", bookDTOList.get(1).getAuthorName());
    }


    @Test
    void testSubscribeToAuthor() {

        String studentEmail = "student@gmail.com";
        String authorName = "Test Name";
        Student student = createStudent(studentEmail);

        User authorUser = new User();
        authorUser.setName(authorName);
        Author author = new Author();
        author.setId(1L);
        author.setUser(authorUser);

        when(studentRepository.findByUserEmail(studentEmail)).thenReturn(student);
        when(authorRepository.findByUserName(authorName)).thenReturn(author);

        ResponseModel<String> responseModel = studentService.subscribeToAuthor(authorName, studentEmail);

        assertNotNull(responseModel);
        assertEquals("Successfully subscribed to author", responseModel.getMessage());

        verify(studentRepository, times(1)).subscribeToAuthor(student.getId(), author.getId());
    }


    private Student createStudent(String email) {
        User user = new User();
        user.setEmail(email);
        Student student = new Student();
        student.setUser(user);
        student.setBooks(new ArrayList<>());
        return student;
    }

    private Book createBook(String name) {
        Book book = new Book();
        book.setName(name);
        return book;
    }
}
