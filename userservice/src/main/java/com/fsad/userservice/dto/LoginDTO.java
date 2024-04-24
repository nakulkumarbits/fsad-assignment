package com.fsad.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDTO {
  @JsonProperty("username")
  @NotBlank(message = "username is mandatory")
  private String userName;

  @JsonProperty("password")
  @NotBlank(message = "password is mandatory")
  private String password;
}
