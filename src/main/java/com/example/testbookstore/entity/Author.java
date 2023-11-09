package com.example.testbookstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "authors")
@Data
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL  )
    @JsonIgnore
    private List<Book> authoredBooks;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private List<Student> students;
}