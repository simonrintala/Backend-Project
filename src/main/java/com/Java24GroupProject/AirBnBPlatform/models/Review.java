package com.Java24GroupProject.AirBnBPlatform.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "reviews")
public class Review {
    @Id
    private String id;

    @DBRef
    @NotNull(message = "review must have a listing")
    private Listing listing;

    @DBRef
    private User user;
    private String username;

    @NotNull(message = "An end date is required")
    private LocalDate endDate;

    @NotNull(message = "rating is a required field")
    @Min(value = 1, message = "rating must be between 1 and 5")
    @Max(value = 5, message = "rating must be between 1 and 5")
    private Double rating;

    @CreatedDate
    private LocalDateTime createdAt;


    public Review() {
    }

    public Review(Listing listing, User user, String username, LocalDate endDate, Double rating) {
        this.listing = listing;
        this.user = user;
        this.username = username;
        this.endDate = endDate;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotNull(message = "review must have a listing") Listing getListing() {
        return listing;
    }

    public void setListing(@NotNull(message = "review must have a listing") Listing listing) {
        this.listing = listing;
    }

    public @NotNull(message = "review must have a user") User getUser() {
        return user;
    }

    public void setUser(@NotNull(message = "review must have a user") User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public @NotNull(message = "An end date is required") LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull(message = "An end date is required") LocalDate endDate) {
        this.endDate = endDate;
    }

    public @NotNull(message = "rating is a required field") @Min(value = 1, message = "rating must be between 1 and 5") @Max(value = 5, message = "rating must be between 1 and 5") Double getRating() {
        return rating;
    }

    public void setRating(@NotNull(message = "rating is a required field") @Min(value = 1, message = "rating must be between 1 and 5") @Max(value = 5, message = "rating must be between 1 and 5") Double rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
