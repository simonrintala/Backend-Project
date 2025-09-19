package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;

/**
 * State pattern: common contract for all booking state handlers.
 * Each state (Pending/Accept/Reject) implements this and knows how to
 * update the booking for that specific state.
 */
public interface IStateHandler {
    /**
     * Apply the "behavior for this state.
     * For example: Pending will set status to "PENDING",
     * Accept will set status to "ACCEPTED",
     * Reject will set status to "REJECTED" and release dates on the listing.
     */
    void apply(Booking booking, Listing listing, ListingRepository listingRepository);
}
