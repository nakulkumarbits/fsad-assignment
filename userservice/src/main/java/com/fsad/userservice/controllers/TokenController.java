package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.TokenDTO;
import com.fsad.userservice.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TokenController implements UserController{
  @Autowired
  private TokenService tokenService;

  @GetMapping("/token/validate")
  public ResponseEntity<Void> validate(@RequestBody TokenDTO tokenDTO) {
    boolean isValid = tokenService.validate(tokenDTO.getToken()) != 0;
    if (isValid) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
