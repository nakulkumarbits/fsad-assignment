package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.UserDTO;
import com.fsad.userservice.entities.User;
import com.fsad.userservice.service.UserService;
import com.fsad.userservice.utils.UserConvertor;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

  private final Logger logger = LoggerFactory.getLogger(RegisterController.class);

  @Autowired
  private UserService userService;

  @Operation(summary = "Registers a user to the portal and records the details of the user.")
  @CrossOrigin
  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
    User user = UserConvertor.toEntity(userDTO);
    logger.debug("user : {}", user);
    return ResponseEntity.ok(userService.register(user));
  }
}
