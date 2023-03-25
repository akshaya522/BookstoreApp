package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookstoreServiceImpl implements BookstoreService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Override
    public String saveBook(BookDto bookDto) {
        Book newBook = new Book();
        newBook.setIsbn(bookDto.getIsbn());
        newBook.setBookGenre(bookDto.getBookGenre());
        newBook.setBookPrice(bookDto.getBookPrice());
        newBook.setBookTitle(bookDto.getBookTitle());
        newBook.setBookPrice(bookDto.getBookPrice());

        List<Author> authorList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        bookDto.getAuthors()
                .forEach(author -> {
                    Author auth = new Author();
                    auth.setName(author.getName());
                    auth.setBirthday(LocalDate.parse(author.getBirthday(), formatter));
                    authorList.add(auth);
                });

        List<Author> authors = authorRepository.saveAll(authorList);
        String authorLst = authors.stream().map(i -> Long.toString(i.getId())).collect(Collectors.joining(","));
        newBook.setAuthorIds(authorLst);
        newBook = bookRepository.save(newBook);
        return newBook.getIsbn();
    }
    @Override
    public Book updateBook(BookDto bookDto) {
        Book bk = new Book();

        if (bk != null) {
            bk.setBookGenre(bookDto.getBookGenre());
            bk.setBookPrice(bookDto.getBookPrice());
            bk.setBookTitle(bookDto.getBookTitle());
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
