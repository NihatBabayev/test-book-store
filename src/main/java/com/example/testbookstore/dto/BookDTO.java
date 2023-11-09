package com.example.testbookstore.dto;

public class BookDTO {
    private String name;
    private String authorName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public BookDTO(String name, String authorName) {
        this.name = name;
        this.authorName = authorName;
    }

    public BookDTO() {
    }
}
