package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import com.example.demo.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    List<Author> findByFullName(String name);
}