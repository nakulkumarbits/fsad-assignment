package com.fsad.bookservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchFilterParams {
  private String author;
  private String title;
  private String genre;
  private String location;
  private String availability;

  private Long userID;
}
