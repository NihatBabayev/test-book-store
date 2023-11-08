package com.example.testbookstore.service;

import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.dto.StudentDTO;
import com.example.testbookstore.repository.StudentRepository;
import com.example.testbookstore.service.Impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testListAllReaders() {
        // Arrange
        String bookName = "SampleBook";
        when(studentRepository.findStudentsByBookName(bookName)).thenReturn(Arrays.asList(
                createStudent("Alice", "alice@example.com"),
                createStudent("Bob", "bob@example.com")
        ));

        // Act
        ResponseModel<List<StudentDTO>> responseModel = bookService.listAllReaders(bookName);

        // Assert
        verify(studentRepository, times(1)).findStudentsByBookName(bookName);
        assertEquals("All students that are currently reading this book", responseModel.getMessage());

        List<StudentDTO> students = responseModel.getData();
        assertEquals(2, students.size());
        assertEquals("Alice", students.get(0).getName());
        assertEquals("alice@example.com", students.get(0).getEmail());
        assertEquals("Bob", students.get(1).getName());
        assertEquals("bob@example.com", students.get(1).getEmail());
    }

    private com.example.testbookstore.entity.Student createStudent(String name, String email) {
        com.example.testbookstore.entity.Student student = new com.example.testbookstore.entity.Student();
        student.set(name);
        student.setEmail(email);
        return student;
    }
}
