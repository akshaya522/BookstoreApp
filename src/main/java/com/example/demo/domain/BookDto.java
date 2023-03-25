package com.example.demo.domain;

import java.util.List;
import java.util.Objects;

public class BookDto {
    private String isbn;
    private String bookTitle;
    private List<AuthorDto> authors;
    private Integer yearOfPublish;
    private Double bookPrice;
    private String bookGenre;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public List<AuthorDto> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDto> authors) {
        this.authors = authors;
    }

    public Integer getYearOfPublish() {
        return yearOfPublish;
    }

    public void setYearOfPublish(Integer yearOfPublish) {
        this.yearOfPublish = yearOfPublish;
    }

    public Double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(Double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(isbn, bookDto.isbn) && Objects.equals(bookTitle, bookDto.bookTitle) && Objects.equals(authors, bookDto.authors) && Objects.equals(yearOfPublish, bookDto.yearOfPublish) && Objects.equals(bookPrice, bookDto.bookPrice) && Objects.equals(bookGenre, bookDto.bookGenre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, bookTitle, authors, yearOfPublish, bookPrice, bookGenre);
    }
}
