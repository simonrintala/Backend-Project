package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import org.springframework.stereotype.Component;

@Component
public class AcceptState implements IStateHandler {
    /**
     * ACCEPTED means the host approved the booking.
     */
    @Override
    public void apply(BookingContext context) {
        context.getBooking().setBookingStatus("ACCEPTED");
    }
}
