package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.LoginDTO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class LoginController {

  @PostMapping("/login")
  public void login(@RequestBody @Valid LoginDTO loginDTO) {

  }
}
