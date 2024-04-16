package com.fsad.userservice.dto;

import java.util.StringJoiner;

public class AddressDTO {
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AddressDTO.class.getSimpleName() + "[", "]")
        .add("addressLine1='" + addressLine1 + "'")
        .add("addressLine2='" + addressLine2 + "'")
        .add("city='" + city + "'")
        .add("state='" + state + "'")
        .toString();
  }
}
