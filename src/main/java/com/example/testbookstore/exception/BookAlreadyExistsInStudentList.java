package com.example.testbookstore.exception;

public class BookAlreadyExistsInStudentList extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Book already exists in the student's list";
    public BookAlreadyExistsInStudentList(){
        super(DEFAULT_MESSAGE);
    }
}
