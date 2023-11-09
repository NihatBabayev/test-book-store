package com.example.testbookstore.repository;

import com.example.testbookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
        Author findByUserEmail(String email);
        @Modifying
        @Query("UPDATE Book b SET b.active = false WHERE b.author.user.email = :userEmail")
        void deactivateBooksByUserEmail(@Param("userEmail") String userEmail);


        Author findByUserName(String authorName);

        @Query("SELECT DISTINCT s.user.email FROM Student s JOIN s.authors a WHERE a.user.email = :authorEmail")
        List<String> findAllSubscribedStudentEmails(@Param("authorEmail") String authorEmail);

}
