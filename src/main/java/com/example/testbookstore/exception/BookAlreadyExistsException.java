package com.example.testbookstore.exception;


public class BookAlreadyExistsException extends RuntimeException{
    private static final String  DEFAULT_MESSAGE = "Book already exists with this name, try another";
    public BookAlreadyExistsException(){
        super(DEFAULT_MESSAGE);
    }

}
