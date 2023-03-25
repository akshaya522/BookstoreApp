package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String>{

    // Optional<Book> findByIsbn(String isbn);
}