package com.fsad.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

@Getter
@Setter
public class AddressDTO {
  private Long id;
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;
  private String pincode;

  @Override
  public String toString() {
    return new StringJoiner(", ", AddressDTO.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("addressLine1='" + addressLine1 + "'")
        .add("addressLine2='" + addressLine2 + "'")
        .add("city='" + city + "'")
        .add("state='" + state + "'")
        .add("pincode='" + pincode + "'")
        .toString();
  }
}
