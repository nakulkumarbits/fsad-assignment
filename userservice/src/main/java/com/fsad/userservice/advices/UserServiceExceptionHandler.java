package com.fsad.userservice.advices;

import com.fsad.userservice.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserServiceExceptionHandler {

  @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> conflict(DataIntegrityViolationException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }
}
