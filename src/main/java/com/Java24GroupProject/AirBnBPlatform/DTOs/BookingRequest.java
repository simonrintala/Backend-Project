package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;


public class BookingRequest {
    private Listing listing;
    private User user;
    private String startDate;
    private String endDate;
    private Integer numberOfGuests;

    public BookingRequest() {
    }

    public Listing getListing() {
        return listing;
    }

    public User getUser() {
        return user;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
