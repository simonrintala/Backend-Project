package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;
import com.Java24GroupProject.AirBnBPlatform.repositories.ListingRepository;
import org.springframework.stereotype.Component;

@Component
public class BookingStateProcessor {

    public void setPending(Booking booking, Listing listing, ListingRepository listingRepository) {
        IStateHandler state = new PendingState();
        state.apply(booking, listing, listingRepository);
    }

    public void accept(Booking booking, Listing listing, ListingRepository listingRepository) {
        IStateHandler state = new AcceptState();
        state.apply(booking, listing, listingRepository);
    }

    public void reject(Booking booking, Listing listing, ListingRepository listingRepository) {
        IStateHandler state = new RejectState();
        state.apply(booking, listing, listingRepository);
    }
}


