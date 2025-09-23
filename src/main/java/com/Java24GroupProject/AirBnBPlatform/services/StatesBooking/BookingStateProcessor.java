package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import org.springframework.stereotype.Component;

/**
 * Simple helper that routes to the right state implementation.
 *
 * Why this:
 * 1. Keeps state selection logic in one place
 * 2. Makes it easy to swap or add new states later
 */
@Component
public class BookingStateProcessor {

    /**
     * Set booking to PENDING state.
     */
    public void setPending(Booking booking, Listing listing, ListingRepository listingRepository) {
        IStateHandler state = new PendingState();
        state.apply(booking, listing, listingRepository);
    }

    /**
     * Set booking to ACCEPTED state.
     */
    public void accept(Booking booking, Listing listing, ListingRepository listingRepository) {
        IStateHandler state = new AcceptState();
        state.apply(booking, listing, listingRepository);
    }

    /**
     * Set booking to REJECTED state and release dates back to the listing.
     */
    public void reject(Booking booking, Listing listing, ListingRepository listingRepository) {
        IStateHandler state = new RejectState();
        state.apply(booking, listing, listingRepository);
    }
}


