package com.fsad.userservice.service;

import com.fsad.userservice.dto.AddressDTO;
import com.fsad.userservice.entities.Address;
import com.fsad.userservice.exceptions.AddressNotFoundException;
import com.fsad.userservice.repository.AddressRepository;
import com.fsad.userservice.utils.AddressConvertor;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

  @Autowired
  private AddressRepository addressRepository;

  public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
    Optional<Address> optionalAddress = addressRepository.findById(addressId);
    if (optionalAddress.isEmpty()) {
      throw new AddressNotFoundException("Address not found : " + addressId);
    }
    Address address = optionalAddress.get();
    if (StringUtils.isNotBlank(addressDTO.getAddressLine1()) && !address.getAddressLine1().equals(addressDTO.getAddressLine1())) {
      address.setAddressLine1(addressDTO.getAddressLine1());
    }
    if (StringUtils.isNotBlank(addressDTO.getAddressLine2()) && !address.getAddressLine2().equals(addressDTO.getAddressLine2())) {
      address.setAddressLine2(addressDTO.getAddressLine2());
    }
    if (StringUtils.isNotBlank(addressDTO.getCity()) && !address.getCity().equals(addressDTO.getCity())) {
      address.setCity(addressDTO.getCity());
    }
    if (StringUtils.isNotBlank(addressDTO.getState()) && !address.getState().equals(addressDTO.getState())) {
      address.setState(addressDTO.getState());
    }
    if (StringUtils.isNotBlank(addressDTO.getPincode()) && !address.getPincode().equals(addressDTO.getPincode())) {
      address.setPincode(addressDTO.getPincode());
    }

    Address savedAddress = addressRepository.save(address);
    return AddressConvertor.toDTO(savedAddress);
  }
}
