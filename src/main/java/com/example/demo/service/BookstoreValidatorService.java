package com.example.demo.service;

import com.example.demo.domain.AuthorDto;
import com.example.demo.domain.Book;
import com.example.demo.domain.BookDto;
import com.example.demo.errors.BadRequestAlertException;
import com.example.demo.repository.BookRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class BookstoreValidatorService {

    private final Integer currYear = Year.now().getValue();

    @Autowired
    BookRepository bookRepository;

    public void validateBook(BookDto bookDto) {
        Book bk = this.bookRepository.findByIsbn(bookDto.getIsbn()).orElse(null);

        if (bk != null) {
            throw new BadRequestAlertException("Book with isbn already exists!");
        }
        if (StringUtils.trimToNull(bookDto.getBookTitle()) == null) {
            throw new BadRequestAlertException("Book title cannot be empty!");
        }
        if (StringUtils.trimToNull(bookDto.getBookTitle()) == null) {
            throw new BadRequestAlertException("Book genre cannot be empty!");
        }
        if (bookDto.getYearOfPublish() == null) {
            throw new BadRequestAlertException("Year of publication cannot be empty!");
        }
        if (bookDto.getYearOfPublish() != null && bookDto.getYearOfPublish() > currYear) {
            throw new BadRequestAlertException("Invalid year of publication!");
        }
        if (bookDto.getAuthors().size() == 0) {
            throw new BadRequestAlertException("Book must have atleast one author!");
        } else {
            this.validateAuthors(bookDto.getAuthors());
        }
    }

    public void validateAuthors(List<AuthorDto> authors) {
        authors
            .forEach(
                auth -> {
                    if (StringUtils.trimToNull(auth.getName()) == null) {
                        throw new BadRequestAlertException("Author name cannot be null");
                    }
                    if (StringUtils.trimToEmpty(auth.getBirthday()) == null) {
                        throw new BadRequestAlertException("Author birthdate cannot be null");
                    }
                    if (!(auth.getBirthday().matches("(^0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4}$)"))) {
                        throw new BadRequestAlertException("Invalid author birthday! Please ensure birthday is in dd-MM-yyyy format");
                    }
                    if (Integer.parseInt(auth.getBirthday().substring(auth.getBirthday().length()-4, auth.getBirthday().length())) > currYear) {
                        throw new BadRequestAlertException("Invalid author birth year");
                    }
                }
            );
    }
}
