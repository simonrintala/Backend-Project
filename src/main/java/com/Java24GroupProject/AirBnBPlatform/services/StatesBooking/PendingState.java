package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import org.springframework.stereotype.Component;

@Component
public class PendingState implements IStateHandler {
    /**
     * PENDING means the host has not decided yet.
     */
    @Override
    public void apply(BookingContext context) {
        context.getBooking().setBookingStatus("PENDING");
    }
}
