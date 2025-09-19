package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
// Removed BookingStatus enum usage; use String status instead
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import org.springframework.stereotype.Component;

@Component
public class PendingState implements IStateHandler {
    /**
     * PENDING means the host has not decided yet.
     * We simply mark the booking as "PENDING". No listing changes are needed.
     */
    @Override
    public void apply(Booking booking, Listing listing, ListingRepository listingRepository) {
        booking.setBookingStatus("PENDING");
    }
}
