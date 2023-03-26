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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookAuthorRepository bookAuthorRepository;

    @Override
    public void saveBooks(List<BookDto> books) {
        books.forEach(book -> saveBook(book));
    };

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
        bookDto
            .getAuthors()
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
    public List<BookAuthor> getBookAuthors(String isbn) {
        return this.bookAuthorRepository.findByBookId(isbn);
    }

    @Override
    public Book updateBook(BookDto bookDto) {
        Book bk = this.bookRepository.findByIsbn(bookDto.getIsbn()).orElse(null);

        if (bk != null) {
            String bkIsbn = bookDto.getIsbn();
            List<BookAuthor> bkAuthors = this.bookAuthorRepository.findByBookId(bkIsbn);

            bk.setYearOfPublish(bookDto.getYearOfPublish());
            bk.setBookGenre(bookDto.getBookGenre());
            bk.setBookPrice(bookDto.getBookPrice());
            bk.setBookTitle(bookDto.getBookTitle());
            bk = this.bookRepository.save(bk);

            List<BookAuthor> bookAuthorList = new ArrayList<>();
            List<Long> authorIds = new ArrayList<>();

            bookDto
                .getAuthors()
                .forEach(author -> {
                    BookAuthor bkAuthor = bkAuthors.stream().filter(i -> i.getAuthorId().equals(author.getId())).findAny().orElse(null);
                    if (author.getId() != null && bkAuthor != null) {
                        authorIds.add(author.getId());
                    } else if (author.getId() != null && bkAuthor == null) {
                        BookAuthor newBkAuthor = new BookAuthor();
                        newBkAuthor.setAuthorId(author.getId());
                        newBkAuthor.setBookId(bkIsbn);
                        authorIds.add(author.getId());
                        bookAuthorList.add(newBkAuthor);
                    } else if (author.getId() == null) {
                        Author auth = new Author();
                        auth.setFullName(author.getName());
                        auth.setBirthday(LocalDate.parse(author.getBirthday(), formatter));
                        auth = this.authorRepository.save(auth);

                        BookAuthor newBkAuthor = new BookAuthor();
                        newBkAuthor.setAuthorId(auth.getId());
                        newBkAuthor.setBookId(bkIsbn);
                        bookAuthorList.add(newBkAuthor);
                    }
                });

            List<BookAuthor> deletedAuth = bkAuthors.stream().filter(i -> !authorIds.contains(i.getAuthorId())).collect(Collectors.toList());
            this.bookAuthorRepository.deleteAll(deletedAuth);
            this.bookAuthorRepository.saveAll(bookAuthorList);

            return bk;
        } else {
            throw new BadRequestAlertException("Invalid isbn value!");
        }
    }

    @Override
    public String deleteBook(String isbn) {
        Book bk = this.bookRepository.findByIsbn(isbn).orElse(null);
        if (bk != null) {
            this.bookRepository.delete(bk);
            return "Book deleted successfully!";
        } else {
            throw new BadRequestAlertException("Isbn does not exist!");
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
