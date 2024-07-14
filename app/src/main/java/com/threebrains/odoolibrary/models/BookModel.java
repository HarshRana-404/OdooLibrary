package com.threebrains.odoolibrary.models;

public class BookModel {
    String  isbn;
    String title;
    String description;
    String author;
    String publisher;
    String year;
    String genre;
    int quantity;
    int issueCount;
    String dateAdded;
    public BookModel(String isbn, String title, String description, String author, String publisher, String year, String genre, int quantity, int issueCount, String dateAdded){
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.genre = genre;
        this.quantity = quantity;
        this.issueCount = issueCount;
        this.dateAdded = dateAdded;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getIssueCount() {
        return issueCount;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
