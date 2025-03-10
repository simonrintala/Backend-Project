package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.IdAndName;

import java.math.BigDecimal;

public class BookingResponse {
    private String id;
    private IdAndName listing;
    private String userId;
    private String username;
    private String userEmail;
    private String userPhoneNr;
    private String startDate;
    private String endDate;
    private Integer numberOfGuests;
    private BigDecimal totalPrice;
    private BookingStatus status;

    public BookingResponse(String id, String listingId, String listingTitle, String userId, String username, String userEmail, String userPhoneNr, String startDate, String endDate, Integer numberOfGuests, BigDecimal totalPrice, BookingStatus status) {
        this.id = id;
        this.listing = new IdAndName(listingId, listingTitle);
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

    public IdAndName getListing() {
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

    public BookingStatus getStatus() {
        return status;
    }
}
