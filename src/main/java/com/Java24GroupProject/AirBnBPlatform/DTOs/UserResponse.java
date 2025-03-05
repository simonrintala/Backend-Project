package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.Role;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.UserAddress;

import java.util.Map;
import java.util.Set;

public class UserResponse {
        private String username;
        private String email;
        private String phoneNr;
        private UserAddress address;
        private String profilePictureURL;
        private String description;
        private Map<String,String> favorites;
        private Set<Role> roles;

    public UserResponse(String username, String email, String phoneNr, UserAddress address, String profilePictureURL, String description, Map<String,String> favorites, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.phoneNr = phoneNr;
        this.address = address;
        this.profilePictureURL = profilePictureURL;
        this.description = description;
        this.favorites = favorites;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Map<String,String> getFavorites() {
        return favorites;
    }

    public void setFavorites(Map<String,String> favorites) {
        this.favorites = favorites;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
