package com.Java24GroupProject.AirBnBPlatform.DTOs;

import jakarta.validation.constraints.*;

public class ReviewRequest {

    @NotNull(message = "listingId is a required field")
    @NotBlank(message = "listingId is a required field")
    @NotEmpty(message = "listingId is a required field")
    private String listingId; // Use listing ID

    @NotNull(message = "rating is a required field")
    @Min(value = 1, message = "rating must be between 1 and 5")
    @Max(value = 5, message = "rating must be between 1 and 5")
    private double rating;

    public ReviewRequest() {
    }

    public @NotNull(message = "listingId is a required field") @NotBlank(message = "listingId is a required field") @NotEmpty(message = "listingId is a required field") String getListingId() {
        return listingId;
    }

    public void setListingId(@NotNull(message = "listingId is a required field") @NotBlank(message = "listingId is a required field") @NotEmpty(message = "listingId is a required field") String listingId) {
        this.listingId = listingId;
    }

    @NotNull(message = "rating is a required field")
    @Min(value = 1, message = "rating must be between 1 and 5")
    @Max(value = 5, message = "rating must be between 1 and 5")
    public double getRating() {
        return rating;
    }

    public void setRating(@NotNull(message = "rating is a required field") @Min(value = 1, message = "rating must be between 1 and 5") @Max(value = 5, message = "rating must be between 1 and 5") double rating) {
        this.rating = rating;
    }
}