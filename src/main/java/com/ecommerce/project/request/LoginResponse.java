package com.ecommerce.project.request;

import java.util.List;

public class LoginResponse {
    private Long id;
    private String JwtToken;
    private String username;
    private List<String> roles;

    public LoginResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;

    }

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoginResponse(Long id,String jwtToken, List<String> roles, String username) {
        this.id = id;
        JwtToken = jwtToken;
        this.roles = roles;
        this.username = username;
    }
}
