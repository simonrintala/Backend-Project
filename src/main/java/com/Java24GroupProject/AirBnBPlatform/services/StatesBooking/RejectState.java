package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.models.supportClasses.BookingStatus;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;

import java.time.LocalDateTime;

public class RejectState implements IStateHandler {
    @Override
    public void apply(Booking booking, Listing listing, ListingRepository listingRepository) {
        listing.addAvailableDateRange(booking.getBookingDates());
        listing.setUpdatedAt(LocalDateTime.now());
        listingRepository.save(listing);
        booking.setBookingStatus(BookingStatus.REJECTED);
    }
}
