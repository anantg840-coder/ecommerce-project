package com.ecommerce.project.service;

import com.ecommerce.project.PayLoad.AddressDto;
import com.ecommerce.project.model.User;
import jakarta.validation.Valid;

import java.util.List;


public interface AddressService {
    AddressDto createAddress(AddressDto addressDto0, User user);

    List<AddressDto> getAllAddresses();

    AddressDto getAddressById(Long addressId);

    List<AddressDto> getUserAddresses(User user);

    AddressDto updateAddress(Long addressId, @Valid AddressDto addressDto);

    String deleteAddress(Long addressId);
}
