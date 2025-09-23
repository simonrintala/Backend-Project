package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;

/**
 * Changed to a Classic State Pattern and added context for Booking state transitions.
 * Holds the current state and domain objects the states "operate" on.
 */
public class BookingContext {
    private IStateHandler currentState;
    private final Booking booking;
    private final Listing listing;
    private final ListingRepository listingRepository;

    public BookingContext(Booking booking, Listing listing, ListingRepository listingRepository) {
        this.booking = booking;
        this.listing = listing;
        this.listingRepository = listingRepository;
    }

    public void transitionTo(IStateHandler newState) {
        this.currentState = newState;
    }

    public void apply() {
        if (currentState == null) {
            throw new IllegalStateException("No state set on BookingContext before apply().");
        }
        currentState.apply(this);
    }

    public Booking getBooking() {
        return booking;
    }

    public Listing getListing() {
        return listing;
    }

    public ListingRepository getListingRepository() {
        return listingRepository;
    }
}


