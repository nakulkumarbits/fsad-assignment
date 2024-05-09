package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.UserDTO;
import com.fsad.userservice.service.TokenService;
import com.fsad.userservice.service.UserService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class UserController {

  @Autowired
  private TokenService tokenService;
  @Autowired
  private UserService userService;

  @Operation(summary = "Fetches the user profile for logged in user.")
  @CrossOrigin
  @GetMapping("/users/{username}")
  public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String token,
                                                @PathVariable("username") String userName) {
    // Auth token should be valid and should be used to access same user's profile.
    if (StringUtils.isNotBlank(token) && tokenService.validate(token)!=0 && tokenService.getUserName(token).equals(userName)) {
      return new ResponseEntity<>(userService.getUserDetails(userName), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @Operation(summary = "Update the user profile for logged in user.")
  @CrossOrigin
  @PatchMapping("/users/{username}")
  public ResponseEntity<UserDTO> updateUser(@RequestHeader("Authorization") String token,
                                            @PathVariable("username") String userName,
                                            @RequestBody @Valid UserDTO userDTO) {
    if (StringUtils.isNotBlank(token) && tokenService.validate(token) != 0 && tokenService.getUserName(token).equals(userName)) {
      return new ResponseEntity<>(userService.updateUser(userName, userDTO), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
