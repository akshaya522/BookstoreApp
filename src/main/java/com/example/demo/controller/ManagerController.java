package com.example.demo.controller;

import com.example.demo.service.BookstoreService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage")
public class ManagerController {

    private final BookstoreService bookstoreService;

    public ManagerController(
        BookstoreService bookstoreService
    ){
        this.bookstoreService = bookstoreService;
    }

    @ApiOperation("Delete a book")
    @DeleteMapping("/deleteBook")
    public String deleteBook(@RequestParam String isbn){
        return this.bookstoreService.deleteBook(isbn);
    }
}
