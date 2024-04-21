package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.LoginDTO;
import com.fsad.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class LoginController implements UserController{

  @Autowired
  private UserService userService;

  @CrossOrigin
  @GetMapping("/login")
  public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
    return ResponseEntity.ok(userService.login(loginDTO.getUserName(), loginDTO.getPassword()));
  }
}
