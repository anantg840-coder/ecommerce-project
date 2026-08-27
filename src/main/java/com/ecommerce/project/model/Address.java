package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long address_id;

    @NotBlank
    @Size(min = 5, message = "street name must be atleast 5 characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "buildingName name must be atleast 5 characters")
    private String buildingName;

    @NotBlank
    @Size(min = 2, message = "city name must be atleast 2 characters")
    private String city;

    @NotBlank
    @Size(min = 2, message = "state name must be atleast 2 characters")
    private String state;

    @NotBlank
    @Size(min = 5, message = "pincode  must be atleast 5 characters")
    private String pincode;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String street, String buildingName, String city, String state, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }


}
