package com.fsad.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

@Setter
@Getter
public class UserDTO {
  @JsonProperty("firstName")
  private String firstName;

  @JsonProperty("lastName")
  private String lastName;

  @JsonProperty("email")
  private String email;

  @JsonProperty("username")
  private String userName;

  @JsonProperty("password")
  private String password;

  private AddressDTO addressDTO;

  @Override
  public String toString() {
    return new StringJoiner(", ", UserDTO.class.getSimpleName() + "[", "]")
        .add("firstName='" + firstName + "'")
        .add("lastName='" + lastName + "'")
        .add("email='" + email + "'")
        .add("userName='" + userName + "'")
        .add("password='" + password + "'")
        .add("addressDTO=" + addressDTO)
        .toString();
  }
}
