package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.LoginDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @PostMapping("/login")
  public void login(@RequestBody LoginDTO loginDTO) {

  }
}
