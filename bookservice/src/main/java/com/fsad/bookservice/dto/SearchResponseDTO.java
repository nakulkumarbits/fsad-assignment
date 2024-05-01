package com.fsad.bookservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponseDTO {
  private List<BookDTO> bookDTOs;
  private int page;
  private int size;
  private int totalPages;
  private long totalElements;
}
