package com.example.demo.controller;

import com.example.demo.service.BookstoreService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class HelloController {

    private final BookstoreService bookstoreService;

    public HelloController(
            BookstoreService bookstoreService
    ){
        this.bookstoreService = bookstoreService;
    }


    @GetMapping("")
    public String test(){
        return "Bookstore Application";
    }

    @DeleteMapping("/deleteBook")
    public String deleteBook(@RequestParam String isbn){
        return this.bookstoreService.deleteBook(isbn);
    }
}
