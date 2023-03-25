package com.example.demo.controller;

import com.example.demo.domain.Book;
import com.example.demo.domain.BookDto;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookstoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookstoreController {
    private final Logger log = LoggerFactory.getLogger(BookstoreController.class);

    @Autowired
    private BookRepository bookRepository;

    private final BookstoreService bookstoreService;

    public BookstoreController(
        BookstoreService bookstoreService
    ){
        this.bookstoreService = bookstoreService;
    }

    @PostMapping("/saveBook")
    public String saveBook(@RequestBody BookDto book){
        return this.bookstoreService.saveBook(book);
    }

    @PutMapping("/updateBook")
    public Book updateBook(@RequestBody BookDto book){
        return this.bookstoreService.updateBook(book);
    }

    @DeleteMapping("/deleteBook")
    public String deleteBook(@RequestParam String isbn){
        return this.bookstoreService.deleteBook(isbn);
    }
}
