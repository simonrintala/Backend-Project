package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
// Removed BookingStatus enum usage; use String status instead
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RejectState implements IStateHandler {
    /**
     * REJECTED means the host declined the booking.
     * We mark the booking as "REJECTED" AND release the previously held dates back to the listing.
     */
    @Override
    public void apply(Booking booking, Listing listing, ListingRepository listingRepository) {
        listing.addAvailableDateRange(booking.getBookingDates());
        listing.setUpdatedAt(LocalDateTime.now());
        listingRepository.save(listing);
        booking.setBookingStatus("REJECTED");
    }
}
