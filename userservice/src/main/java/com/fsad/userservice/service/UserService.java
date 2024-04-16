package com.fsad.userservice.service;

import com.fsad.userservice.dto.UserDTO;
import com.fsad.userservice.entities.User;
import com.fsad.userservice.exceptions.UnauthorizedException;
import com.fsad.userservice.exceptions.UserNotFoundException;
import com.fsad.userservice.repository.UserRepository;
import com.fsad.userservice.utils.UserConvertor;
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
        if (existingToken != null) {
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
}
