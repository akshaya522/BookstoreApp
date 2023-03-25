package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.domain.BookDto;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class BookstoreServiceImpl implements BookstoreService {

    @Autowired
    BookRepository bookRepository;

//    public BookstoreServiceImpl(
//        BookRepository bookRepository
//    ){
//        this.bookRepository = bookRepository;
//    }

    @Override
    public String saveBook(BookDto bookDto) {
        Book newBook = new Book();
        newBook.setIsbn(bookDto.getIsbn());
//        newBook.setGenre(bookDto.getGenre());
//        newBook.setPrice(bookDto.getPrice());
//        newBook.setTitle(bookDto.getTitle());
//        newBook.setPrice(bookDto.getPrice());
        newBook = bookRepository.save(newBook);
        return newBook.getIsbn();
    }
    @Override
    public Book updateBook(BookDto bookDto) {
        Book bk = new Book();

        if (bk != null) {
//            bk.setGenre(bookDto.getGenre());
//            bk.setPrice(bookDto.getPrice());
//            bk.setTitle(bookDto.getTitle());
//            bk.setPrice(bookDto.getPrice());
            bk = bookRepository.save(bk);
            return bk;
        }
        return bk;
    }

    @Override
    public String deleteBook(String isbn) {
        Book bk = new Book();
        if (bk != null) {
            bookRepository.delete(bk);
            return "Book deleted successfully!";
        } else {
            return "Book with this isbn does not exist!";
        }
    }
}
