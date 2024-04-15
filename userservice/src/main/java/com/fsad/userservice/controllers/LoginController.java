package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.LoginDTO;
import com.fsad.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class LoginController {

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public void login(@RequestBody @Valid LoginDTO loginDTO) {
    userService.login(loginDTO.getUserName(), loginDTO.getPassword());
  }
}
