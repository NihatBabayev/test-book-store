package com.example.testbookstore;

import com.example.testbookstore.dto.ResponseModel;
import com.example.testbookstore.dto.StudentDTO;
import com.example.testbookstore.entity.User;
import com.example.testbookstore.repository.StudentRepository;
import com.example.testbookstore.service.Impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
class BookServiceUnitTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        bookService = new BookServiceImpl(studentRepository);
    }

    @Test
    void testListAllReaders() {

        String bookName = "SampleBook";
        User user1 = createUser("Murad", "murad@gmail.com");
        User user2 = createUser("Valeh", "valeh@gmail.com");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);


        when(studentRepository.findStudentsByBookName(bookName)).thenReturn(userList);


        ResponseModel<List<StudentDTO>> responseModel = bookService.listAllReaders(bookName);


        verify(studentRepository, times(1)).findStudentsByBookName(bookName);
        assertEquals("All students that are currently reading this book", responseModel.getMessage());

        List<StudentDTO> students = responseModel.getData();
        assertEquals(2, students.size());
        assertEquals("Murad", students.get(0).getName());
        assertEquals("murad@gmail.com", students.get(0).getEmail());
        assertEquals("Valeh", students.get(1).getName());
        assertEquals("valeh@gmail.com", students.get(1).getEmail());
    }

    private User createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }


}
