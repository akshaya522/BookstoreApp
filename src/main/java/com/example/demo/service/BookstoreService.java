package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.domain.BookAuthor;
import com.example.demo.domain.BookDto;

import javax.management.BadAttributeValueExpException;
import java.util.List;

public interface BookstoreService {
    BookDto saveBook(BookDto book);
    BookDto updateBook(BookDto bookDto);
    String deleteBook(String isbn);
    List<BookDto> searchForBook(String title, String author);
    void saveBooks(List<BookDto> books);
    Book getBook(String id);
    List<BookAuthor> getBookAuthors(String isbn);
}
