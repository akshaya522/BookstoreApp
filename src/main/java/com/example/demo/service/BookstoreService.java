package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.domain.BookAuthor;
import com.example.demo.domain.BookDto;

import javax.management.BadAttributeValueExpException;
import java.util.List;

public interface BookstoreService {
    String saveBook(BookDto book);

    void saveBooks(List<BookDto> books);

    Book updateBook(BookDto bookDto) throws BadAttributeValueExpException;

    String deleteBook(String isbn);

    Book getBook(String id);

    List<Book> searchForBook(String title, String author);

    List<BookAuthor> getBookAuthors(String isbn);
}
