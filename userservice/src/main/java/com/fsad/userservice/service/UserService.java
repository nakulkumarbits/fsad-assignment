package com.fsad.userservice.service;

import com.fsad.userservice.entities.User;
import com.fsad.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public User register(User user) {
    return userRepository.save(user);
  }
}
