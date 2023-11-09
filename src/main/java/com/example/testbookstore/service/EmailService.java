package com.example.testbookstore.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmailService {
    void sendEmail(String to, String subject, String text);

    void sendEmailsAsync(List<String> emails, String subject, String message);
}
