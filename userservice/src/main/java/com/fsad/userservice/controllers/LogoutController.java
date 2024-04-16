package com.fsad.userservice.controllers;

import com.fsad.userservice.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

  @Autowired
  private TokenService tokenService;

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
    tokenService.invalidate(token);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
