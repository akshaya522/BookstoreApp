package com.example.demo.controller;

import com.example.demo.BadRequestAlertException;
import com.example.demo.domain.Book;
import com.example.demo.domain.BookAuthor;
import com.example.demo.domain.BookDto;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookstoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.BadAttributeValueExpException;
import java.util.List;

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

    @PostMapping("/")
    public String test(){
        return "Bookstore Application";
    }

    @PostMapping("/saveBook")
    public String saveBook(@RequestBody BookDto book){
        return this.bookstoreService.saveBook(book);
    }

    @PostMapping("/saveBooks")
    public void saveBooks(@RequestBody List<BookDto> books){
        this.bookstoreService.saveBooks(books);
    }

    @PutMapping("/updateBook")
    public Book updateBook(@RequestBody BookDto book) throws BadAttributeValueExpException {
        return this.bookstoreService.updateBook(book);
    }

    @GetMapping("/getBook/{id}")
    public Book getBook(@PathVariable String id) throws BadRequestAlertException {
        return this.bookstoreService.getBook(id);
    }

    @GetMapping("/getBookAuthors/{id}")
    public List<BookAuthor> getBookAuthors(@PathVariable String id) throws BadRequestAlertException {
        return this.bookstoreService.getBookAuthors(id);
    }

    @DeleteMapping("/deleteBook")
    public String deleteBook(@RequestParam String isbn){
        return this.bookstoreService.deleteBook(isbn);
    }

    @GetMapping("/searchBook")
    public List<Book> searchBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String authorName) {
        return this.bookstoreService.searchForBook(title, authorName);
    }
}
