package com.ecommerce.project.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 2, max = 20)
    private String username;
    @NotBlank
    @Size(min = 2, max = 50)
    @Email
    private String email;

    private Set<String> roles;

    @NotBlank
    @Size(min = 2, max = 40)
    private String password;




}
