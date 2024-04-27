package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.AddressDTO;
import com.fsad.userservice.exceptions.UnauthorizedException;
import com.fsad.userservice.service.AddressService;
import com.fsad.userservice.service.TokenService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AddressController {
  @Autowired
  private AddressService addressService;

  @Autowired
  private TokenService tokenService;

  @PatchMapping("/address/{addressId}")
  public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,
                                                  @RequestBody AddressDTO addressDTO,
                                                  @RequestHeader("Authorization") String token) {
    if (StringUtils.isNotBlank(token) && tokenService.validate(token) != 0) {
      return new ResponseEntity<>(addressService.updateAddress(addressId, addressDTO), HttpStatus.OK);
    } else {
      throw new UnauthorizedException("Auth header missing");
    }
  }
}
