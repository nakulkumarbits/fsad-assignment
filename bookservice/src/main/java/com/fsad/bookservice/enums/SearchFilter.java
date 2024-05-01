package com.fsad.bookservice.enums;

import java.util.HashSet;
import java.util.Set;

public enum SearchFilter {
  GENRE("genre"),
  AUTHOR("author"),
  TITLE("title"),
  LOCATION("location"),
  AVAILABILITY("availability");

  private String name;

  SearchFilter(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static final Set<String> SEARCH_FILTERS;

  static {
    SEARCH_FILTERS = new HashSet<>();
    for (SearchFilter value : SearchFilter.values()) {
      SEARCH_FILTERS.add(value.getName());
    }
  }
}
