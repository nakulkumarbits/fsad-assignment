package com.fsad.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
  @NotBlank(message = "username is mandatory")
  private String userName;

  @NotBlank(message = "password is mandatory")
  private String password;

  public LoginDTO(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
