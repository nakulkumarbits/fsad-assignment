package com.fsad.bookservice.advices;

import com.fsad.bookservice.dto.ErrorResponseDTO;
import com.fsad.bookservice.exceptions.InvalidOrderStateException;
import com.fsad.bookservice.exceptions.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Order not found")
  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleOrderNotFoundException(OrderNotFoundException ex) {
    return new ResponseEntity<>(new ErrorResponseDTO("Order not found"), HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid order state found")
  @ExceptionHandler(InvalidOrderStateException.class)
  public ResponseEntity<ErrorResponseDTO> handleInvalidOrderStateException(InvalidOrderStateException ex) {
    return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
    return new ResponseEntity<>(new ErrorResponseDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }
}
