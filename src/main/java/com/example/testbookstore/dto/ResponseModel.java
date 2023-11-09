package com.example.testbookstore.dto;

import lombok.Data;

@Data
public class ResponseModel<T> {
    T data;
    String message;
}