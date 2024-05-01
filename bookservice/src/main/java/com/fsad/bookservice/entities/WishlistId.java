package com.fsad.bookservice.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class WishlistId implements Serializable {
  private Long bookId;
  private Long userId;
}
