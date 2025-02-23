package com.Java24GroupProject.AirBnBPlatform.DTOs;

import java.math.BigDecimal;
import java.util.Date;

public class BookingResponse {
    private String bookingId;
    private String title;
    private String host;
    private String username;
    private String email;
    private String phoneNr;
    private Date startDate;
    private Date endDate;
    private Integer numberOfGuests;
    private BigDecimal totalPrice;

    public BookingResponse(String bookingId, String title, String host, String username, String email, String phoneNr, Date startDate, Date endDate, Integer numberOfGuests, BigDecimal totalPrice) {
        this.bookingId = bookingId;
        this.title = title;
        this.host = host;
        this.username = username;
        this.email = email;
        this.phoneNr = phoneNr;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getTitle() {
        return title;
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
