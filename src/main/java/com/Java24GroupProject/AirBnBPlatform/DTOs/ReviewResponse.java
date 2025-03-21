package com.Java24GroupProject.AirBnBPlatform.DTOs;

import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.IdAndName;

import java.time.LocalDateTime;

public class ReviewResponse {
    private String id;
    private String listingId;
    private IdAndName user;
    private double rating;
    private LocalDateTime createdAt;

    public ReviewResponse(String id, String listingId, String userId, String username, double rating, LocalDateTime createdAt) {
        this.id = id;
        this.listingId = listingId;
        this.user = new IdAndName(userId, username);
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getListingId() {
        return listingId;
    }

    public IdAndName getUser() {
        return user;
    }

    public double getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}

