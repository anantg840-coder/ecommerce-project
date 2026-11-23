package com.ecommerce.project.PayLoad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
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
    @Size(min = 5, message = "pincode must be atleast 5 characters")
    private String pincode;
}