package com.fsad.userservice.utils;

import com.fsad.userservice.dto.UserDTO;
import com.fsad.userservice.entities.User;

public class UserConvertor {
  public static User toEntity(UserDTO userDTO) {
    if (userDTO == null) {
      return null;
    }
    User user = new User();
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());
    user.setUserName(userDTO.getUserName());
    user.setEmail(userDTO.getEmail());
    user.setPassword(userDTO.getPassword());
    user.setAddress(AddressConvertor.toEntity(userDTO.getAddressDTO()));
    return user;
  }

  public static UserDTO toDTO(User user) {
    if (user == null) {
      return null;
    }
    UserDTO userDTO = new UserDTO();
    userDTO.setFirstName(user.getFirstName());
    userDTO.setLastName(user.getLastName());
    userDTO.setUserName(user.getUserName());
    userDTO.setEmail(user.getEmail());
    userDTO.setPassword(user.getPassword());
    userDTO.setAddressDTO(AddressConvertor.toDTO(user.getAddress()));
    return userDTO;
  }
}
