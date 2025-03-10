package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.UserAddress;

import java.time.LocalDateTime;
import java.util.Set;

public class UserResponse {
        private String id;
        private String username;
        private String email;
        private String phoneNr;
        private UserAddress address;
        private String profilePictureURL;
        private String description;
        private Set<Role> roles;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    public UserResponse(String id, String username, String email, String phoneNr, UserAddress address, String profilePictureURL, String description, Set<Role> roles, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNr = phoneNr;
        this.address = address;
        this.profilePictureURL = profilePictureURL;
        this.description = description;
        this.roles = roles;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public UserAddress getAddress() {
        return address;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
