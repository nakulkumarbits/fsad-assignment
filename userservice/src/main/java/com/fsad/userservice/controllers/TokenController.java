package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.TokenDTO;
import com.fsad.userservice.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
  @Autowired
  private TokenService tokenService;

  @PostMapping("/token/validate")
  public ResponseEntity<Object> validate(@RequestBody TokenDTO tokenDTO) {
    Long userID = tokenService.validate(tokenDTO.getToken());
    boolean isValid = userID != 0;
    if (isValid) {
      return new ResponseEntity<>(userID, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(0L, HttpStatus.UNAUTHORIZED);
    }
  }
}
