package com.example.testbookstore.repository;

import com.example.testbookstore.entity.Book;
import com.example.testbookstore.entity.Student;
import com.example.testbookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUserEmail(String email);

    @Query("SELECT DISTINCT u FROM User u JOIN Student s ON u.id = s.user.id JOIN s.books b WHERE b.name = :bookName and b.active=true")
    List<User> findStudentsByBookName(@Param("bookName") String bookName);

    @Query("SELECT b FROM Book b JOIN b.students s JOIN s.user u WHERE u.email = :email and b.active=true ")
    List<Book> findBooksByStudent(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO student_author (student_id, author_id) VALUES (:studentId, :authorId)",
            nativeQuery = true)
    void subscribeToAuthor(Long studentId, Long authorId);

}
