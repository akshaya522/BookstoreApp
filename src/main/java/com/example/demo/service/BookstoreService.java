package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.domain.BookDto;

public interface BookstoreService {
    public String saveBook(BookDto book);

    Book updateBook(BookDto bookDto);

    String deleteBook(String isbn);
}
