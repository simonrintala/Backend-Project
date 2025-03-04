package com.Java24GroupProject.AirBnBPlatform.DTOs;

public class ReviewRequest {
    private String listingId; // Use listing ID
    private String hostId;    // Use host ID
    private double rating;


    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}