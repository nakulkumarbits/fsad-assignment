package com.fsad.userservice.service;

import com.fsad.userservice.dto.UserDTO;
import com.fsad.userservice.entities.User;
import com.fsad.userservice.repository.UserRepository;
import com.fsad.userservice.utils.UserConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public UserDTO register(User user) {
    User savedUser = userRepository.save(user);
    return UserConvertor.toDTO(savedUser);
  }
}
