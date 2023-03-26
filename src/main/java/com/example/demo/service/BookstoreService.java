package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.domain.BookDto;

import javax.management.BadAttributeValueExpException;
import java.util.List;

public interface BookstoreService {
    public String saveBook(BookDto book);

    Book updateBook(BookDto bookDto) throws BadAttributeValueExpException;

    String deleteBook(String isbn);

    Book getBook(String id);

    List<Book> searchForBook(String title, String author);
}
