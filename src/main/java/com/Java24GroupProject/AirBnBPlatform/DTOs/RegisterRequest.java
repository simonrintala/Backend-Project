package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

//SHOULD BE EXPANDED UPON when User class gets more variables
//SHOULD BE ANNOTATED (e.g., @NotNull, etc.)
//The DTO for receiving information for user registration
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String phoneNr;
    private Set<Role> roles;


    public RegisterRequest(String username, String password, String email, String phoneNr, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNr = phoneNr;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
