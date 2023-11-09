package com.example.testbookstore;

import com.example.testbookstore.dto.UserDTO;
import com.example.testbookstore.entity.Student;
import com.example.testbookstore.entity.User;
import com.example.testbookstore.exception.UserAlreadyExistsException;
import com.example.testbookstore.repository.AuthorRepository;
import com.example.testbookstore.repository.StudentRepository;
import com.example.testbookstore.repository.UserRepository;
import com.example.testbookstore.service.EmailService;
import com.example.testbookstore.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, authorRepository, studentRepository, passwordEncoder, emailService);
    }

    @Test
    void saveUser_whenUserExists_shouldThrowUserAlreadyExistsException() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("existinguser@example.com");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(new User());

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(userDTO, "student"));

        // Verify
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verifyNoMoreInteractions(userRepository, authorRepository, studentRepository, passwordEncoder, emailService);
    }

    @Test
    void saveUser_whenUserDoesNotExist_shouldSaveUserAndRelatedEntity() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("newuser@example.com");
        userDTO.setPassword("superpasswd");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

        // Act
        userService.saveUser(userDTO, "student");

        // Verify
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(emailService, times(1)).sendEmail(eq(userDTO.getEmail()), anyString(), anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verifyNoMoreInteractions(userRepository, authorRepository, studentRepository, passwordEncoder, emailService);
    }
}
