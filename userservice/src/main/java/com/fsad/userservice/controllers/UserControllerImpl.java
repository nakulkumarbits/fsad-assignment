package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.UserDTO;
import com.fsad.userservice.entities.User;
import com.fsad.userservice.repository.UserRepository;
import com.fsad.userservice.service.UserService;
import com.fsad.userservice.utils.UserConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserControllerImpl implements UserController{

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> listOfUsers = userRepository.findAll();

        List<UserDTO> userDTOS = new ArrayList<>();
        listOfUsers.forEach(user -> userDTOS.add(UserConvertor.toDTO(user)));

        return ResponseEntity.ok(userDTOS);
    }
}
