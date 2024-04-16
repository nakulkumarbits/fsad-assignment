package com.fsad.bookservice.dto;

import java.util.StringJoiner;

public class BookDTO {
  private Long id;
  private String title;
  private String author;
  private String genre;
  private String bookCondition;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getBookCondition() {
    return bookCondition;
  }

  public void setBookCondition(String bookCondition) {
    this.bookCondition = bookCondition;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", BookDTO.class.getSimpleName() + "[", "]")
        .add("title='" + title + "'")
        .add("author='" + author + "'")
        .add("genre='" + genre + "'")
        .add("bookCondition='" + bookCondition + "'")
        .toString();
  }
}
