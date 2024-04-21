package com.fsad.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.StringJoiner;

public class UserDTO {
  @JsonProperty("firstName")
  private String firstName;
  @JsonProperty("lastName")
  private String lastName;
  @JsonProperty("email")
  private String email;
  @JsonProperty("userName")
  private String userName;
  @JsonProperty("password")
  private String password;
  private AddressDTO addressDTO;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public AddressDTO getAddressDTO() {
    return addressDTO;
  }

  public void setAddressDTO(AddressDTO addressDTO) {
    this.addressDTO = addressDTO;
  }

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
