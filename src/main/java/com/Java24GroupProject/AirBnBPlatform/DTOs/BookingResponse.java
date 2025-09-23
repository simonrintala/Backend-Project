package com.Java24GroupProject.AirBnBPlatform.DTOs;

// Removed BookingStatus enum usage; use String for status
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.NestedListing;

import java.math.BigDecimal;

public class BookingResponse {
    private String id;
    private NestedListing listing;
    private String userId;
    private String username;
    private String userEmail;
    private String userPhoneNr;
    private String startDate;
    private String endDate;
    private Integer numberOfGuests;
    private BigDecimal totalPrice;
    private String status;

    public BookingResponse(String id, NestedListing listing, String userId, String username, String userEmail, String userPhoneNr, String startDate, String endDate, Integer numberOfGuests, BigDecimal totalPrice, String status) {
        this.id = id;
        this.listing = listing;
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
        this.userPhoneNr = userPhoneNr;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public NestedListing getListing() {
        return listing;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhoneNr() {
        return userPhoneNr;
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

    public String getStatus() {
        return status;
    }
}
