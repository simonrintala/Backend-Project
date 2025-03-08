package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;

import java.math.BigDecimal;

public class BookingResponse {
    private String id;
    private String listingTitle;
    private String username;
    private String email;
    private String phoneNr;
    private String startDate;
    private String endDate;
    private Integer numberOfGuests;
    private BigDecimal totalPrice;
    private BookingStatus status;

    public BookingResponse(String id, String listingTitle, String username, String email, String phoneNr, String startDate, String endDate, Integer numberOfGuests, BigDecimal totalPrice, BookingStatus status) {
        this.id = id;
        this.listingTitle = listingTitle;
        this.username = username;
        this.email = email;
        this.phoneNr = phoneNr;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getListingTitle() {
        return listingTitle;
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

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }
}
