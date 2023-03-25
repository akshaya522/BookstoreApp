package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.repository.BookRepository;

import java.util.Arrays;

public class BookstoreServiceImpl implements BookstoreService {

    private final BookRepository bookRepository;

    public BookstoreServiceImpl (
            BookRepository bookRepository
    ){
        this.bookRepository = bookRepository;
    }

    public String saveBook(Book book){
        Book newBook = new Book();
        newBook.setIsbn(book.getIsbn());
        newBook.setAuthors(book.getAuthors());
        newBook.setGenre(book.getGenre());
        newBook.setPrice(book.getPrice());
        newBook.setTitle(book.getTitle());
        newBook.setPrice(book.getPrice());
        newBook = bookRepository.saveAndFlush(newBook);
        return newBook.getIsbn();
    }

    public Book updateBook(Book book) {
        Book bk = this.bookRepository.findByIsbn(book.getIsbn()).orElse(null);

        if (bk != null) {
            bk.setAuthors(book.getAuthors());
            bk.setGenre(book.getGenre());
            bk.setPrice(book.getPrice());
            bk.setTitle(book.getTitle());
            bk.setPrice(book.getPrice());
            bk = bookRepository.saveAndFlush(bk);
            return bk;
        }
        return book;
    }

    public void deleteBook(String isbn) {
        Book bk = this.bookRepository.findByIsbn(isbn).orElse(null);
        if (bk != null) {
            this.bookRepository.deleteAllByIdInBatch(Arrays.asList(bk.getIsbn()));
        }
    }
}
