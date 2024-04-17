package com.fsad.userservice.controllers;

import com.fsad.userservice.dto.AddressDTO;
import com.fsad.userservice.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AddressController {
  @Autowired
  private AddressService addressService;

  @PatchMapping("/address/{addressId}")
  public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,
                                                  @RequestBody AddressDTO addressDTO) {
    return new ResponseEntity<>(addressService.updateAddress(addressId, addressDTO), HttpStatus.OK);
  }
}
