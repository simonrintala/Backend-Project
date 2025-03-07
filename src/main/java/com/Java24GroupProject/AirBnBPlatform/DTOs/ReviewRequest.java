package com.Java24GroupProject.AirBnBPlatform.DTOs;

public class ReviewRequest {
    private String listingId; // Use listing ID
    private double rating;

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}