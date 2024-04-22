package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.UserDTO;
import com.fsad.userservice.service.TokenService;
import com.fsad.userservice.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

  @Autowired
  private TokenService tokenService;
  @Autowired
  private UserService userService;

  @CrossOrigin
  @GetMapping("/userprofile/{username}")
  public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String token,
                                                @PathVariable("username") String userName) {
    // Auth token should be valid and should be used to access same user's profile.
    if (StringUtils.isNotBlank(token) && tokenService.validate(token) && tokenService.getUserName(token).equals(userName)) {
      return new ResponseEntity<>(userService.getUserDetails(userName), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
}
