package com.Java24GroupProject.AirBnBPlatform.DTOs;

import java.time.LocalDateTime;

public class ReviewResponse {
    private String id;
    private String title;
    private double rating;
    private LocalDateTime createdAt;
    //subject to change, wasn't 100% sure what we should return
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

