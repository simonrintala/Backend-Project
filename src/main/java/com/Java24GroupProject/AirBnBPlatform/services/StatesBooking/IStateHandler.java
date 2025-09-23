package com.Java24GroupProject.AirBnBPlatform.services.StatesBooking;

/**
 * State pattern: contract for Booking states operating on a BookingContext.
 */
public interface IStateHandler {
    /**
     * Apply this state's behavior using the provided context.
     */
    void apply(BookingContext context);
}
