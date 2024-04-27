package com.fsad.bookservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fsad.bookservice.enums.BookCondition;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({AuditingEntityListener.class})
@ToString
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String author;

  @Column(nullable = false)
  private String genre;

  @Enumerated(value = EnumType.STRING)
  private BookCondition bookCondition;

  @Column(nullable = false)
  private String publisher;

  @Column(nullable = false)
  private Long userID;

  @Column(nullable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(nullable = false)
  @LastModifiedDate
  private LocalDateTime modifiedDate;

  @Version
  private Long version;
}
