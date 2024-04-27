package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.LoginDTO;
import com.fsad.userservice.service.UserService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class LoginController {

  @Autowired
  private UserService userService;

  @CrossOrigin
  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
    String token = userService.login(loginDTO.getUserName(), loginDTO.getPassword());
    JSONObject tokenJson = new JSONObject();
    tokenJson.put("token", token);
    return ResponseEntity.ok(tokenJson.toString());
  }
}
