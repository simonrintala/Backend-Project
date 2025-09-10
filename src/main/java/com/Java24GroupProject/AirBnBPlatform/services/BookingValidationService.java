package com.Java24GroupProject.AirBnBPlatform.services;

import com.Java24GroupProject.AirBnBPlatform.DTOs.BookingRequest;
import com.Java24GroupProject.AirBnBPlatform.exceptions.IllegalArgumentException;
import com.Java24GroupProject.AirBnBPlatform.exceptions.ResourceNotFoundException;
import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.User;
import com.Java24GroupProject.AirBnBPlatform.repositories.BookingRepository;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;

public interface BookingValidationService {
    //validate that BookingRequest data is valid
    default void validateBooking(BookingRequest bookingRequest, User currentUser, ListingRepository listingRepository) {

        //check that listing id is valid
        Listing listing = validateListingIdAndGetListing(bookingRequest, listingRepository);

        //check that the user for the booking is not also the host of the listing
        if (currentUser.getId().equals(listing.getHost().getId())) {
            throw new IllegalArgumentException("user not allowed to make booking for their own listing");
        }

        //check that nrOfGuest does not exceed listing capacity
        if (bookingRequest.getNumberOfGuests() > listing.getCapacity()) {
            throw new IllegalArgumentException("nrOfGuest on the booking exceeds listing capacity");
        }

    }

    //validate id and get booking object
    default Booking validateBookingIdAndGetBooking(String id, BookingRepository bookingRepository) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No booking with id '" + id + "' in database"));
    }

    //validate listing id and get listing object from booking
    default Listing validateListingIdAndGetListing(Booking booking, ListingRepository listingRepository) {
        return ListingService.validateListingIdAndGetListing(booking.getListing().getId(), listingRepository);
    }

    //validate listing id and get listing object from bookingRequest
    default Listing validateListingIdAndGetListing(BookingRequest bookingRequest, ListingRepository listingRepository) {
        return ListingService.validateListingIdAndGetListing(bookingRequest.getListingId(), listingRepository);

    }
}
