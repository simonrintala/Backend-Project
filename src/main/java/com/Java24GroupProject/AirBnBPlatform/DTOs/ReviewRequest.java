package com.Java24GroupProject.AirBnBPlatform.DTOs;

public class ReviewRequest {
    private String bookingId;
    private Double rating;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
