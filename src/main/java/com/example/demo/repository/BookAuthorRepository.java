package com.example.demo.repository;

import java.util.List;

import com.example.demo.domain.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, String> {

    List<BookAuthor> findByBookIdIn(List<String> bookId);
    List<BookAuthor> findByAuthorIdIn(List<Long> authorId);
}