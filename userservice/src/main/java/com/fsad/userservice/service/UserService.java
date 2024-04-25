package com.fsad.userservice.service;

import com.fsad.userservice.dto.UserDTO;
import com.fsad.userservice.entities.User;
import com.fsad.userservice.exceptions.UnauthorizedException;
import com.fsad.userservice.exceptions.UserNotFoundException;
import com.fsad.userservice.repository.UserRepository;
import com.fsad.userservice.utils.UserConvertor;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TokenService tokenService;

  public UserDTO register(User user) {
    User savedUser = userRepository.save(user);
    return UserConvertor.toDTO(savedUser);
  }

  public String login(final String userName, final String password) {
    Optional<User> optionalUser = userRepository.findByUserName(userName);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (user.getPassword().equals(password)) {
        String existingToken = tokenService.tokenExistsForKey(user.getUserName());
        if (existingToken != null && tokenService.validate(existingToken)!=0) {
          return existingToken;
        }
        String token = tokenService.createJWT(user.getUserName(), user.getEmail());
        tokenService.storeToken(user.getUserName(), token);
        return token;
      }
      throw new UnauthorizedException("Invalid username/password");
    }
    throw new UserNotFoundException("User not found : " + userName);
  }

  public UserDTO getUserDetails(String userName) {
    Optional<User> optionalUser = userRepository.findByUserName(userName);
    if (optionalUser.isPresent()) {
      return UserConvertor.toDTO(optionalUser.get());
    }
    throw new UserNotFoundException("User not found : " + userName);
  }

  public UserDTO updateUser(String userName, UserDTO userDTO) {
    Optional<User> optionalUser = userRepository.findByUserName(userName);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (StringUtils.isNotBlank(userDTO.getEmail())) {
        user.setEmail(userDTO.getEmail());
      }
      if (StringUtils.isNotBlank(userDTO.getFirstName())) {
        user.setFirstName(userDTO.getFirstName());
      }
      if (StringUtils.isNotBlank(userDTO.getLastName())) {
        user.setLastName(userDTO.getLastName());
      }
      if (StringUtils.isNotBlank(userDTO.getAddressDTO().getAddressLine1())) {
        user.getAddress().setAddressLine1(userDTO.getAddressDTO().getAddressLine1());
      }
      if (StringUtils.isNotBlank(userDTO.getAddressDTO().getAddressLine2())) {
        user.getAddress().setAddressLine2(userDTO.getAddressDTO().getAddressLine2());
      }
      if (StringUtils.isNotBlank(userDTO.getAddressDTO().getCity())) {
        user.getAddress().setCity(userDTO.getAddressDTO().getCity());
      }
      if (StringUtils.isNotBlank(userDTO.getAddressDTO().getState())) {
        user.getAddress().setState(userDTO.getAddressDTO().getState());
      }
      if (StringUtils.isNotBlank(userDTO.getAddressDTO().getPincode())) {
        user.getAddress().setPincode(userDTO.getAddressDTO().getPincode());
      }

      User updatedUser = userRepository.save(user);
      return UserConvertor.toDTO(updatedUser);
    }
    throw new UserNotFoundException("User not found : " + userName);
  }
}
