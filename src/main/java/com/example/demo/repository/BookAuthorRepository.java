package com.example.demo.repository;

import java.util.List;
import com.example.demo.domain.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, String> {

    List<BookAuthor> findByBookId(String bookId);
    List<BookAuthor> findByBookIdIn(List<String> bookId);
    List<BookAuthor> findByAuthorIdIn(List<Long> authorId);
}