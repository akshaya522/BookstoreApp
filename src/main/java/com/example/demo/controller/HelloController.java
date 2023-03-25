package com.example.demo.controller;

import com.example.demo.domain.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Akshaya Muthu!";
    }

    @PostMapping("/saveBook")
    public String saveShow(@RequestBody Book book) {
        // showRepository.save(show);
        return "Show saved...";
    }
}
