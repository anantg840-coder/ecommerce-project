package com.ecommerce.project.controller;

import com.ecommerce.project.PayLoad.AddressDto;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto0){
        User user = authUtil.loggedInUser();
        AddressDto savedaddressDto = addressService.createAddress(addressDto0,user);
        return new ResponseEntity<>(savedaddressDto, HttpStatus.CREATED);
    }
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAllAddresses(){
        List<AddressDto> addressDtoList = addressService.getAllAddresses();
        return new ResponseEntity<List<AddressDto>>(addressDtoList,HttpStatus.OK);

    }
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long addressId){
        AddressDto addressDto = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDto,HttpStatus.OK);
    }
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDto>> getUserAddresses(){
        User user = authUtil.loggedInUser();
        List<AddressDto> addressDtoList = addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressDtoList,HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long addressId,@Valid @RequestBody AddressDto addressDto){
        AddressDto updatedAddress = addressService.updateAddress(addressId,addressDto);
        return new ResponseEntity<>(updatedAddress,HttpStatus.OK);

    }
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status,HttpStatus.OK);

    }

}
