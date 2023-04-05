package com.example.demo.controller;

import com.example.demo.errors.BadRequestAlertException;
import com.example.demo.domain.Book;
import com.example.demo.domain.BookAuthor;
import com.example.demo.domain.BookDto;
import com.example.demo.service.BookstoreService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.management.BadAttributeValueExpException;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookstoreController {
    private final BookstoreService bookstoreService;
    public BookstoreController(
        BookstoreService bookstoreService
    ){
        this.bookstoreService = bookstoreService;
    }

    @ApiOperation("Save a book")
    @PostMapping("/saveBook")
    public BookDto saveBook(@RequestBody BookDto book){
        return this.bookstoreService.saveBook(book);
    }

    @ApiOperation("Save a list of books")
    @PostMapping("/saveBooks")
    public void saveBooks(@RequestBody List<BookDto> books){
        this.bookstoreService.saveBooks(books);
    }

    @ApiOperation("Update details of existing book")
    @PutMapping("/updateBook")
    public BookDto updateBook(@RequestBody BookDto book) throws BadAttributeValueExpException {
        return this.bookstoreService.updateBook(book);
    }

    @ApiOperation("Retrieve existing book details")
    @GetMapping("/getBook/{id}")
    public Book getBook(@PathVariable String id) throws BadRequestAlertException {
        return this.bookstoreService.getBook(id);
    }

    @ApiOperation("Retrieve existing book and author details")
    @GetMapping("/getBookAuthors/{id}")
    public List<BookAuthor> getBookAuthors(@PathVariable String id) throws BadRequestAlertException {
        return this.bookstoreService.getBookAuthors(id);
    }

    @ApiOperation("Search for books by exact title and/or author exact full name")
    @GetMapping("/searchBook")
    public List<BookDto> searchBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String authorName) {
        return this.bookstoreService.searchForBook(title, authorName);
    }
}
