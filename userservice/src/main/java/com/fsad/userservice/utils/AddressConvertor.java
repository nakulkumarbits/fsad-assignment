package com.fsad.userservice.utils;

import com.fsad.userservice.dto.AddressDTO;
import com.fsad.userservice.entities.Address;

public class AddressConvertor {
  public static Address toEntity(AddressDTO addressDTO) {
    if (addressDTO == null) {
      return null;
    }
    Address address = new Address();
    address.setAddressLine1(addressDTO.getAddressLine1());
    address.setAddressLine2(addressDTO.getAddressLine2());
    address.setCity(addressDTO.getCity());
    address.setState(addressDTO.getState());
    return address;
  }

  public static AddressDTO toDTO(Address address) {
    if (address == null) {
      return null;
    }
    AddressDTO addressDTO = new AddressDTO();
    addressDTO.setId(address.getId());
    addressDTO.setAddressLine1(address.getAddressLine1());
    addressDTO.setAddressLine2(address.getAddressLine2());
    addressDTO.setCity(address.getCity());
    addressDTO.setState(address.getState());
    return addressDTO;
  }
}
