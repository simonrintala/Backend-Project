package com.Java24GroupProject.AirBnBPlatform.DTOs;

import java.math.BigDecimal;
import java.util.Date;

public class BookingRequest {
    private String id;
    private String title;
    private String host;
    private Date startDate;
    private Date endDate;
    private Integer numberOfGuests;
    private BigDecimal totalPrice;

    public BookingRequest(String id, String title, String host, Date startDate, Date endDate, Integer numberOfGuests, BigDecimal totalPrice) {
        this.id = id;
        this.title = title;
        this.host = host;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
