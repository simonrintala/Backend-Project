package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;

public class PendingState implements IStateHandler {
    @Override
    public void apply(Booking booking, Listing listing, ListingRepository listingRepository) {
        booking.setBookingStatus(BookingStatus.PENDING);
    }
}
