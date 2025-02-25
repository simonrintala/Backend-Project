package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;

import java.util.Date;

public class BookingRequest {
    private Listing listing;
    private User user;
    private Date startDate;
    private Date endDate;
    private Integer numberOfGuests;

    public BookingRequest() {
    }

    public Listing getListing() {
        return listing;
    }

    public User getUser() {
        return user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
