package com.example.testbookstore.exception;

public class BookNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Book doesn't exist";
    public BookNotFoundException(){
        super(DEFAULT_MESSAGE);
    }
}
