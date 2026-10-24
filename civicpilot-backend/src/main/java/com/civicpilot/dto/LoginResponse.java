package com.civicpilot.dto;

import com.civicpilot.entity.Role;

public class LoginResponse {

    private String token;
    private String type;
    private Role role;

    public LoginResponse(String token, String type, Role role) {
        this.token = token;
        this.type = type;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public Role getRole() {
        return role;
    }
}