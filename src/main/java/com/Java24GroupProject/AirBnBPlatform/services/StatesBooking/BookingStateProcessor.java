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
    private final PendingState pendingState;
    private final AcceptState acceptState;
    private final RejectState rejectState;

    public BookingStateProcessor(PendingState pendingState, AcceptState acceptState, RejectState rejectState) {
        this.pendingState = pendingState;
        this.acceptState = acceptState;
        this.rejectState = rejectState;
    }

    /**
     * Set booking to PENDING state.
     */
    public void setPending(Booking booking, Listing listing, ListingRepository listingRepository) {
        pendingState.apply(booking, listing, listingRepository);
    }

    /**
     * Set booking to ACCEPTED state.
     */
    public void accept(Booking booking, Listing listing, ListingRepository listingRepository) {
        acceptState.apply(booking, listing, listingRepository);
    }

    /**
     * Set booking to REJECTED state and release dates back to the listing.
     */
    public void reject(Booking booking, Listing listing, ListingRepository listingRepository) {
        rejectState.apply(booking, listing, listingRepository);
    }
}


