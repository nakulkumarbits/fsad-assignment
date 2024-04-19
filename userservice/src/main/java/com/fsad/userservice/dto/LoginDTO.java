package com.fsad.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
  @JsonProperty("username")
  @NotBlank(message = "username is mandatory")
  private String userName;

  @JsonProperty("password")
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
