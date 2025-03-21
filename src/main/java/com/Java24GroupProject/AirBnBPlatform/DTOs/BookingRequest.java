package com.Java24GroupProject.AirBnBPlatform.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {
    @NotNull(message = "listingId is a required field")
    @NotBlank(message = "listingId is a required field")
    @NotEmpty(message = "listingId is a required field")
    private String listingId;

    @NotNull(message = "booking startDate is a required field")
    private String startDate;

    @NotNull(message = "booking endDate is a required field")
    private String endDate;

    @NotNull(message = "numberOfGuests is a required field")
    @Positive(message = "numberOfGuests must be greater than 0")
    private Integer numberOfGuests;

    public BookingRequest() {
    }

    public @NotNull(message = "listingId is a required field") @NotBlank(message = "listingId is a required field") @NotEmpty(message = "listingId is a required field") String getListingId() {
        return listingId;
    }

    public void setListingId(@NotNull(message = "listingId is a required field") @NotBlank(message = "listingId is a required field") @NotEmpty(message = "listingId is a required field") String listingId) {
        this.listingId = listingId;
    }

    public @NotNull(message = "booking startDate is a required field") String getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull(message = "booking startDate is a required field") String startDate) {
        this.startDate = startDate;
    }

    public @NotNull(message = "booking endDate is a required field") String getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull(message = "booking endDate is a required field") String endDate) {
        this.endDate = endDate;
    }

    public @NotNull(message = "numberOfGuests is a required field") @Positive(message = "numberOfGuests must be greater than 0") Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(@NotNull(message = "numberOfGuests is a required field") @Positive(message = "numberOfGuests must be greater than 0") Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
