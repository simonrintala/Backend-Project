package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RejectState implements IStateHandler {
    /**
     * REJECTED means the host declined the booking.
     */
    @Override
    public void apply(BookingContext context) {
        context.getListing().addAvailableDateRange(context.getBooking().getBookingDates());
        context.getListing().setUpdatedAt(LocalDateTime.now());
        context.getListingRepository().save(context.getListing());
        context.getBooking().setBookingStatus("REJECTED");
    }
}
