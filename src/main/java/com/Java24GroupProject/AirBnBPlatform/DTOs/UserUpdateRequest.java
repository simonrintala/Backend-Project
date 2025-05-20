package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.UserAddress;

public class UserUpdateRequest {
    private String email;
   private String PhoneNr;
    private UserAddress address;

    // Getters and setters


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNr() {
        return PhoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        PhoneNr = phoneNr;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }
}
