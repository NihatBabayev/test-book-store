package com.example.testbookstore.service;

import com.example.testbookstore.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void saveUser(UserDTO userDTO, String type);

    boolean isUserExists(String email);
}
