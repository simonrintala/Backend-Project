package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import jakarta.validation.constraints.*;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Set;

//The DTO for receiving information for user registration
public class RegisterRequest {
    @NotNull(message = "username is a required field")
    @NotEmpty(message = "username is a required field")
    @NotBlank(message = "username is a required field")
    @Indexed(unique = true)
    private String username;

    @NotNull(message = "password is a required field")
    @NotEmpty(message = "password is a required field")
    @NotBlank(message = "password is a required field")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$",
            message = "password must be minimum 8 characters and must contain must contain: 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character")

    private String password;

    @NotNull(message = "email is a required field")
    @NotEmpty(message = "email is a required field")
    @NotBlank(message = "email is a required field")
    @Email(message = "email does not have a valid format")
    @Indexed(unique = true)
    private String email;

    @NotNull(message = "phoneNr is a required field")
    @NotEmpty(message = "phoneNr is a required field")
    @NotBlank(message = "phoneNr is a required field")
    @Pattern(regexp = "^[0-9+]+$", message = "phoneNr may only contain numbers and +")
    @Indexed(unique = true)
    private String phoneNr;

    private String street;
    private String zipCode;
    private String city;
    private String country;

    private String profilePictureURL;

    private String description;

    private Set<Role> roles;

    public RegisterRequest(String username, String password, String email, String phoneNr, String street, String zipCode, String city, String country, String profilePictureURL, String description, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNr = phoneNr;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.profilePictureURL = profilePictureURL;
        this.description = description;
        this.roles = roles;
    }

    public @NotNull(message = "username is a required field") @NotEmpty(message = "username is a required field") @NotBlank(message = "username is a required field") String getUsername() {
        return username;
    }

    public @NotNull(message = "password is a required field") @NotEmpty(message = "password is a required field") @NotBlank(message = "password is a required field") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,}$",
            message = "password must be minimum 8 characters and must contain must contain: 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character") String getPassword() {
        return password;
    }

    public @NotNull(message = "email is a required field") @NotEmpty(message = "email is a required field") @NotBlank(message = "email is a required field") @Email(message = "email does not have a valid format") String getEmail() {
        return email;
    }

    public @NotNull(message = "phoneNr is a required field") @NotEmpty(message = "phoneNr is a required field") @NotBlank(message = "phoneNr is a required field") @Pattern(regexp = "^[0-9+]+$", message = "phoneNr may only contain numbers and +") String getPhoneNr() {
        return phoneNr;
    }

    public String getStreet() {
        return street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public String getDescription() {
        return description;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
