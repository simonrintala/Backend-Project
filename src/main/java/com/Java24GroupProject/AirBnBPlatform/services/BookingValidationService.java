package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;

public interface BookingValidationService {
    //validate that BookingRequest data is valid
    default void validateBooking(BookingRequest bookingRequest, User currentUser, Listing listing) {

        //check that the user for the booking is not also the host of the listing
        if (currentUser.getId().equals(listing.getHost().getId())) {
            throw new IllegalArgumentException("user not allowed to make booking for their own listing");
        }

        //check that nrOfGuest does not exceed listing capacity
        if (bookingRequest.getNumberOfGuests() > listing.getCapacity()) {
            throw new IllegalArgumentException("nrOfGuest on the booking exceeds listing capacity");
        }

    }

}
