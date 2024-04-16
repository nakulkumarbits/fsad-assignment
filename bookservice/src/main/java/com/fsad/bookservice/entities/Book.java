package com.fsad.bookservice.entities;

import com.fsad.bookservice.enums.BookCondition;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private String author;
  @Column(nullable = false)
  private String genre;
  @Enumerated(value = EnumType.STRING)
  private BookCondition bookCondition;
  @CreatedDate
  private LocalDateTime createdDate;
  @LastModifiedDate
  private LocalDateTime modifiedDate;
  @Version
  private Long version;

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

  public BookCondition getBookCondition() {
    return bookCondition;
  }

  public void setBookCondition(BookCondition bookCondition) {
    this.bookCondition = bookCondition;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Book.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("title='" + title + "'")
        .add("author='" + author + "'")
        .add("genre='" + genre + "'")
        .add("bookCondition=" + bookCondition)
        .add("createdDate=" + createdDate)
        .add("modifiedDate=" + modifiedDate)
        .add("version=" + version)
        .toString();
  }
}
