package com.example.demo.service;

import com.example.demo.errors.BadRequestAlertException;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookstoreServiceImpl implements BookstoreService {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookAuthorRepository bookAuthorRepository;
    @Autowired
    BookstoreValidatorService bookstoreValidatorService;

    /**
     * This function validates book and author details and
     * saves book, it's authors and creates a BookAuthor
     * @param bookDto - dto with book and author details
     * @return - book saved
     * */
    @Override
    public BookDto saveBook(BookDto bookDto) {
        this.bookstoreValidatorService.validateBook(bookDto);

        /** Save book */
        Book newBook = new Book();
        newBook.setIsbn(bookDto.getIsbn());
        newBook.setBookGenre(bookDto.getBookGenre());
        newBook.setBookPrice(bookDto.getBookPrice());
        newBook.setBookTitle(bookDto.getBookTitle());
        newBook.setBookPrice(bookDto.getBookPrice());
        newBook.setYearOfPublish(bookDto.getYearOfPublish());
        newBook = this.bookRepository.save(newBook);

        /** Save book authors */
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
        List<AuthorDto> authorDtos = this.authorsToDtos(authors);
        bookDto.setAuthors(authorDtos);

        /** Save bookAuthor for each author - entity to connect book and author */
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

        return bookDto;
    }

    /**
     * This function validates book and author details and
     * updates existing book details and authors
     * @param bookDto - dto with updates book and author details
     * @return - isbn of book saved
     * */
    @Override
    public BookDto updateBook(BookDto bookDto) {
        this.bookstoreValidatorService.validateBook(bookDto);
        Book book = this.bookRepository.findByIsbn(bookDto.getIsbn()).orElse(null);

        /** Check if book exists */
        if (book != null) {
            String bkIsbn = bookDto.getIsbn();
            /** Update book details and save */
            book.setYearOfPublish(bookDto.getYearOfPublish());
            book.setBookGenre(bookDto.getBookGenre());
            book.setBookPrice(bookDto.getBookPrice());
            book.setBookTitle(bookDto.getBookTitle());
            book = this.bookRepository.save(book);

            /** Get existing bookAuthors */
            List<BookAuthor> bkAuthors = this.bookAuthorRepository.findByBookId(bkIsbn);
            List<BookAuthor> bookAuthorList = new ArrayList<>();
            List<Long> updatedAuthorIds = new ArrayList<>();

            bookDto
                .getAuthors()
                .forEach(author -> {
                    BookAuthor bkAuthor = bkAuthors.stream().filter(i -> i.getAuthorId().equals(author.getId())).findAny().orElse(null);
                    /** Check if bookAuthor already saved for author */
                    if (author.getId() != null && bkAuthor != null) {
                        updatedAuthorIds.add(author.getId());
                        /** Author saved but no bookAuthor saved */
                    } else if (author.getId() != null && bkAuthor == null) {
                        BookAuthor newBkAuthor = new BookAuthor();
                        newBkAuthor.setAuthorId(author.getId());
                        newBkAuthor.setBookId(bkIsbn);
                        updatedAuthorIds.add(author.getId());
                        bookAuthorList.add(newBkAuthor);
                        /** Author not saved and no bookAuthor saved */
                    } else if (author.getId() == null) {
                        Author auth = new Author();
                        auth.setFullName(author.getName());
                        auth.setBirthday(LocalDate.parse(author.getBirthday(), formatter));
                        auth = this.authorRepository.save(auth);
                        author.setId(auth.getId());

                        BookAuthor newBkAuthor = new BookAuthor();
                        newBkAuthor.setAuthorId(auth.getId());
                        newBkAuthor.setBookId(bkIsbn);
                        bookAuthorList.add(newBkAuthor);
                    }
                });

            /** Find deleted authors and delete bookAuthors */
            List<BookAuthor> deletedAuth = bkAuthors.stream().filter(i -> !updatedAuthorIds.contains(i.getAuthorId())).collect(Collectors.toList());
            this.bookAuthorRepository.deleteAll(deletedAuth);
            this.bookAuthorRepository.saveAll(bookAuthorList);

            return bookDto;
        } else {
            throw new BadRequestAlertException("Book with isbn does not exist!");
        }
    }

    /**
     * This function deletes an existing book
     * @param isbn - isbn of book to be deleted
     * */
    @Override
    public String deleteBook(String isbn) {
        Book book = this.bookRepository.findByIsbn(isbn).orElse(null);

        /** Check if book exists */
        if (book != null) {
            this.bookRepository.delete(book);
            return "Book deleted successfully!";
        } else {
            throw new BadRequestAlertException("Book with isbn does not exist!");
        }
    }

    /**
     * This function searches the bookstore by book title and/or author full name
     * @param title - title of book to search for
     * @param authorName - fullName of author to search for
     * @return - list of books found, empty list if no books found
     * */
    @Override
    public List<BookDto> searchForBook(String title, String authorName) {
        List<BookDto> books = new ArrayList<>();
        /** Search by both book title and author name */
        if (StringUtils.trimToNull(title) != null && StringUtils.trimToNull(authorName) != null) {
            /** Find books with matching title */
            List<Book> bk = this.bookRepository.findByBookTitle(title.trim());
            /** Find authors with matching authorName */
            List<Author> authors = this.authorRepository.findByFullName(authorName);
            List<Long> athList = authors.stream().map(Author::getId).collect(Collectors.toList());
            /** Find bookAuthors with matching book Ids */
            List<BookAuthor> bkAthList = this.bookAuthorRepository.findByBookIdIn(bk.stream().map(Book::getIsbn).collect(Collectors.toList()));
            List<String> valid = new ArrayList<>();

            bkAthList
                .stream()
                .forEach(i -> {
                    /** Check if book has matching author */
                    if (athList.contains(i.getAuthorId())) {
                        valid.add(i.getBookId());
                    }
                });


            books.addAll(this.booksToDtosWithAuthorDtos(bk.stream().filter(i -> valid.contains(i.getIsbn())).collect(Collectors.toList()), bkAthList, authors));
        /** Only book title */
        } else if (StringUtils.trimToNull(title) != null) {
            List<Book> bk = this.bookRepository.findByBookTitle(title.trim());
            List<BookAuthor> bkAuthors = this.bookAuthorRepository.findByBookIdIn(bk.stream().map(Book::getIsbn).collect(Collectors.toList()));
            List<Author> authors = this.authorRepository.findByIdIn(bkAuthors.stream().map(BookAuthor::getAuthorId).collect(Collectors.toList()));
            if (bk != null && bk.size() > 0){
                books.addAll(this.booksToDtosWithAuthorDtos(bk, bkAuthors, authors));
            }
        /** Only Author name */
        } else if (StringUtils.trimToNull(authorName) != null) {
            /** Find authors with matching name */
            List<Author> authors = this.authorRepository.findByFullName(authorName);
            List<Long> athList = authors.stream().map(Author::getId).collect(Collectors.toList());
            /** Find bookAuthors for found authors */
            List<BookAuthor> bkAthList = this.bookAuthorRepository.findByAuthorIdIn(athList);
            /** Find books */
            List<Book> bkList = this.bookRepository.findByIsbnIn(bkAthList.stream().map(BookAuthor::getBookId).collect(Collectors.toList()));
            books.addAll(this.booksToDtosWithAuthorDtos(bkList, bkAthList, authors));
        } else {
            throw new BadRequestAlertException("No book title or author name entered!");
        }

        return books;
    }

    @Override
    public void saveBooks(List<BookDto> books) {
        books.forEach(book -> saveBook(book));
    };

    @Override
    public Book getBook(String id) {
        Book bk = this.bookRepository.findByIsbn(id).orElse(null);
        return bk;
    }

    @Override
    public List<BookAuthor> getBookAuthors(String isbn) {
        return this.bookAuthorRepository.findByBookId(isbn);
    }

    private List<AuthorDto> authorsToDtos(List<Author> authors) {
        List<AuthorDto> authorDtos = new ArrayList<>();
        authors
            .forEach(i -> {
                AuthorDto dto = new AuthorDto();
                dto.setId(i.getId());
                dto.setName(i.getFullName());
                dto.setBirthday(String.valueOf(i.getBirthday()));
                authorDtos.add(dto);
            });

        return authorDtos;
    }

    private List<BookDto> booksToDtos(List<Book> books) {
        List<BookDto> bookDtos = new ArrayList<>();
        books
            .forEach(i -> {
                BookDto dto = new BookDto();
                dto.setIsbn(i.getIsbn());
                dto.setBookGenre(i.getBookGenre());
                dto.setBookPrice(i.getBookPrice());
                dto.setBookTitle(i.getBookTitle());
                dto.setYearOfPublish(i.getYearOfPublish());
                bookDtos.add(dto);
            });

        return bookDtos;
    }

    private List<BookDto> booksToDtosWithAuthorDtos(List<Book> books, List<BookAuthor> bookAuthorList, List<Author> authors) {
        List<BookDto> bookDtos = this.booksToDtos(books);
        bookDtos
            .forEach(bk -> {
                List<Long> athIds = bookAuthorList.stream().filter(i -> i.getBookId().equals(bk.getIsbn())).map(BookAuthor::getAuthorId).collect(Collectors.toList());
                List<Author> auths = authors.stream().filter(i -> athIds.contains(i.getId())).collect(Collectors.toList());
                bk.setAuthors(this.authorsToDtos(auths));
            });

        return bookDtos;
    }
}
