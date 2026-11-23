package com.ecommerce.project.service;

import com.ecommerce.project.Exception.ApiException;
import com.ecommerce.project.PayLoad.AddressDto;
import com.ecommerce.project.Repositries.AddressRepository;
import com.ecommerce.project.Repositries.UserRepository;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDto createAddress(AddressDto addressDto, User user) {
        Address address = modelMapper.map(addressDto, Address.class);
        List<Address> addressList = user.getAddress();
        addressList.add(address);
        user.setAddress(addressList);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddresses() {
        List<Address> addressList = addressRepository.findAll();
        if (addressList.isEmpty()) {
            throw new ApiException("No address found");
        }
        return addressList.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();
    }

    @Override
    public AddressDto getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ApiException("Address not found"));
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getUserAddresses(User user) {
        List<Address> addresses = user.getAddress();
        if (addresses.isEmpty()) {
            throw new ApiException("No address found");
        }
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();
    }

    @Override
    public AddressDto updateAddress(Long addressId, AddressDto addressDto) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ApiException("Address not found"));
        address.setCity(addressDto.getCity());
        address.setPincode(addressDto.getPincode());
        address.setStreet(addressDto.getStreet());
        address.setState(addressDto.getState());
        address.setBuildingName(addressDto.getBuildingName());
        Address savedAddress = addressRepository.save(address);

        User user = address.getUser();
        user.getAddress().removeIf(address1 -> address1.getAddress_id().equals(addressId));
        user.getAddress().add(savedAddress);
        userRepository.save(user);
        return modelMapper.map(savedAddress, AddressDto.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ApiException("Address not found"));
        User user = address.getUser();
        user.getAddress().removeIf(address1 -> address1.getAddress_id().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(address);
        return "address has been deleted sucessfully with addressId " + addressId;
    }
}