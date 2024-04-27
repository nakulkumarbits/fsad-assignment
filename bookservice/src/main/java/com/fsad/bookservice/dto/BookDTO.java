package com.fsad.bookservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fsad.bookservice.enums.BookCondition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookDTO {
  private Long id;
  private String title;
  private String author;
  private String genre;
  @JsonProperty("bookCondition")
  private BookCondition bookCondition;
  private String publisher;
  @JsonProperty("userId")
  private Long userID;
}
