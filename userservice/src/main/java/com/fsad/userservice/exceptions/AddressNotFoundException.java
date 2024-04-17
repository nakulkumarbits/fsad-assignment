package com.fsad.userservice.exceptions;

public class AddressNotFoundException extends RuntimeException {
  public AddressNotFoundException(String message) {
    super(message);
  }
}
