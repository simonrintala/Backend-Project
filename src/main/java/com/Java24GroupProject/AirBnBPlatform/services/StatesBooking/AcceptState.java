package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
// Removed BookingStatus enum usage; use String status instead
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;

public class AcceptState implements IStateHandler {
    @Override
    public void apply(Booking booking, Listing listing, ListingRepository listingRepository) {
        booking.setBookingStatus("ACCEPTED");
    }
}
