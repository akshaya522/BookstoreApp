package com.example.demo.service;

import com.example.demo.BadRequestAlertException;
import com.example.demo.domain.*;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookAuthorRepository;
import com.example.demo.repository.BookRepository;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    BookAuthorRepository bookAuthorRepository;

    @Override
    public String saveBook(BookDto bookDto) {
        Book newBook = new Book();
        newBook.setIsbn(bookDto.getIsbn());
        newBook.setBookGenre(bookDto.getBookGenre());
        newBook.setBookPrice(bookDto.getBookPrice());
        newBook.setBookTitle(bookDto.getBookTitle());
        newBook.setBookPrice(bookDto.getBookPrice());
        newBook = this.bookRepository.save(newBook);

        List<Author> authorList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        bookDto.getAuthors()
                .forEach(author -> {
                    Author auth = new Author();
                    auth.setFullName(author.getName());
                    auth.setBirthday(LocalDate.parse(author.getBirthday(), formatter));
                    authorList.add(auth);
                });

        List<Author> authors = this.authorRepository.saveAll(authorList);

        String bookId = newBook.getIsbn();
        List<BookAuthor> bookAuthorList = new ArrayList<>();
        authors
                .stream()
                .forEach(i -> {
                    BookAuthor bkAuthor = new BookAuthor();
                    bkAuthor.setAuthorId(i.getId());
                    bkAuthor.setBookId(bookId);
                    bookAuthorList.add(bkAuthor);
                });
        this.bookAuthorRepository.saveAll(bookAuthorList);

        return newBook.getIsbn();
    }

    @Override
    public Book getBook(String id) {
        Book bk = this.bookRepository.findByIsbn(id).orElse(null);
        return bk;
    }

    @Override
    public Book updateBook(BookDto bookDto) {
        Book bk = this.bookRepository.findByIsbn(bookDto.getIsbn()).orElse(null);

        if (bk != null) {
            bk.setYearOfPublish(bookDto.getYearOfPublish());
            bk.setBookGenre(bookDto.getBookGenre());
            bk.setBookPrice(bookDto.getBookPrice());
            bk.setBookTitle(bookDto.getBookTitle());

            bk = this.bookRepository.save(bk);
            return bk;
        } else {
            throw new BadRequestAlertException("Invalid isbn value!");
        }
    }

    @Override
    public String deleteBook(String isbn) {
        Book bk = new Book();
        if (bk != null) {
            this.bookRepository.delete(bk);
            return "Book deleted successfully!";
        } else {
            return "Book with this isbn does not exist!";
        }
    }

    @Override
    public List<Book> searchForBook(String title, String author) {
        List<Book> books = new ArrayList<>();

        if (StringUtils.trimToNull(title) != null && StringUtils.trimToNull(author) != null) {
            List<Book> bk = this.bookRepository.findByBookTitle(title.trim());
            List<Long> athList = this.authorRepository.findByFullName(author).stream().map(Author::getId).collect(Collectors.toList());
            List<BookAuthor> bkAthList = this.bookAuthorRepository.findByBookIdIn(bk.stream().map(Book::getIsbn).collect(Collectors.toList()));
            List<String> valid = new ArrayList<>();

            bkAthList
                    .stream()
                    .forEach(i -> {
                        if (athList.contains(i.getAuthorId())) {
                            valid.add(i.getBookId());
                        }
                    });
            books.addAll(bk.stream().filter(i -> valid.contains(i.getIsbn())).collect(Collectors.toList()));

        } else if (StringUtils.trimToNull(title) != null) {
            List<Book> bk = this.bookRepository.findByBookTitle(title.trim());
            if (bk != null && bk.size() > 0){
                books.addAll(bk);
            }
        } else if (StringUtils.trimToNull(author) != null) {
            List<Long> athList = this.authorRepository.findByFullName(author).stream().map(Author::getId).collect(Collectors.toList());
            List<BookAuthor> bkAthList = this.bookAuthorRepository.findByAuthorIdIn(athList);
            List<Book> bkList = this.bookRepository.findByIsbnIn(bkAthList.stream().map(BookAuthor::getBookId).collect(Collectors.toList()));
            books.addAll(bkList);
        } else {
            throw new BadRequestAlertException("Invalid title or author");
        }

        return books;
    }
}
