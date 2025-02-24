package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.UserAddress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class UserResponse {

        private String username;
        private String hashedPassword;
        private String email;

        private String phoneNr;
        private UserAddress address;
        private String profilePictureURL;

        private String description;
        private List<String> favorites;
        private Set<Role> roles;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    public UserResponse(String username, String hashedPassword, String email, String phoneNr, UserAddress address, String profilePictureURL, String description, List<String> favorites, Set<Role> roles, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.email = email;
        this.phoneNr = phoneNr;
        this.address = address;
        this.profilePictureURL = profilePictureURL;
        this.description = description;
        this.favorites = favorites;
        this.roles = roles;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
