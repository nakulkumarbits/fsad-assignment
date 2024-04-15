package com.fsad.userservice.advices;

import com.fsad.userservice.dto.ErrorResponse;
import com.fsad.userservice.exceptions.UnauthorizedException;
import com.fsad.userservice.exceptions.UserNotFoundException;
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

  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Username or password is invalid")
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
    return new ResponseEntity<>(new ErrorResponse("Username or password is invalid"), HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Username or password is incorrect")
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
    return new ResponseEntity<>(new ErrorResponse("Username or password is incorrect"), HttpStatus.UNAUTHORIZED);
  }
}
