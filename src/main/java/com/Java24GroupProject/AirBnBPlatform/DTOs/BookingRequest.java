package com.Java24GroupProject.AirBnBPlatform.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BookingRequest {
    @NotNull(message = "A listing ID is required")
    private String listingId;

    @NotNull(message = "booking start and end dates are required")
    private String startDate;

    @NotNull(message = "booking start and end dates are required")
    private String endDate;

    @NotNull(message = "Number of guests is required")
    @Positive(message = "Amount of guests must be greater than 0")
    private Integer numberOfGuests;

    public BookingRequest() {
    }

    public @NotNull(message = "A listing ID is required") String getListingId() {
        return listingId;
    }

    public void setListingId(@NotNull(message = "A listing ID is required") String listingId) {
        this.listingId = listingId;
    }

    public @NotNull(message = "booking start and end dates are required") String getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull(message = "booking start and end dates are required") String startDate) {
        this.startDate = startDate;
    }

    public @NotNull(message = "booking start and end dates are required") String getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull(message = "booking start and end dates are required") String endDate) {
        this.endDate = endDate;
    }

    public @NotNull(message = "Number of guests is required") @Positive(message = "Amount of guests must be greater than 0") Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(@NotNull(message = "Number of guests is required") @Positive(message = "Amount of guests must be greater than 0") Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
