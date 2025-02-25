package com.Java24GroupProject.AirBnBPlatform.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reviews")
public class Review {
    @Id
    private String id;

    @DBRef
    @NotNull(message = "A listing ID is required")
    private Listing listing;

    @DBRef
    @NotNull(message = "A user is required")
    private User user;

    @NotNull (message = "end date cannot be null")
    private LocalDateTime endDate;

    @Positive (message = "Review score must be greater than zero")
    @Min(value = 1, message = "Minimum value must be atleast 1")
    @Max(value = 5, message = "Maximum value cannot be more than 5")
    private Double rating;

    //private Double totalReviews;

    @CreatedDate
    private LocalDateTime createdAt;

    public Review() {
    }

    public Review(String id, Listing listing, User user, LocalDateTime endDate, Double rating, LocalDateTime createdAt) {
        this.id = id;
        this.listing = listing;
        this.user = user;
        this.endDate = endDate;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotNull(message = "A listing ID is required") Listing getListing() {
        return listing;
    }

    public void setListing(@NotNull(message = "A listing ID is required") Listing listing) {
        this.listing = listing;
    }

    public @NotNull(message = "A user is required") User getUser() {
        return user;
    }

    public void setUser(@NotNull(message = "A user is required") User user) {
        this.user = user;
    }

    public @NotNull(message = "end date cannot be null") LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull(message = "end date cannot be null") LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public @Positive(message = "Review score must be greater than zero") @Min(value = 1, message = "Minimum value must be atleast 1") @Max(value = 5, message = "Maximum value cannot be more than 5") Double getRating() {
        return rating;
    }

    public void setRating(@Positive(message = "Review score must be greater than zero") @Min(value = 1, message = "Minimum value must be atleast 1") @Max(value = 5, message = "Maximum value cannot be more than 5") Double rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
