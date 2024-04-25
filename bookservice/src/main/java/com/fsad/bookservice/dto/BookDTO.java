package com.fsad.bookservice.dto;

import com.fsad.bookservice.enums.BookCondition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookDTO {
  private long id;
  private String title;
  private String author;
  private String genre;
  private BookCondition bookCondition;
  private String publisher;
  private Long userID;

}
