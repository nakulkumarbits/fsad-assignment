package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.UserDTO;
import com.fsad.userservice.entities.User;
import com.fsad.userservice.service.UserService;
import com.fsad.userservice.utils.UserConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController implements UserController{

  private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

  @Autowired
  private UserService userService;

  @CrossOrigin
  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
    User user = UserConvertor.toEntity(userDTO);
    logger.debug("user : {}", user);
    return ResponseEntity.ok(userService.register(user));
  }
}
