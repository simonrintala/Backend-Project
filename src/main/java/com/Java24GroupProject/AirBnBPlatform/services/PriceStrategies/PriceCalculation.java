package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class PriceCalculation {
    
    PriceCalculation() {
    }
    
    //calculate price from nr of booked days and price per night from listing
    public BigDecimal calculateAndSetPrice(Booking booking, Listing listing) {
        //calculate days in between start and end date
        long daysBetween = ChronoUnit.DAYS.between(
                booking.getBookingDates().getStartDate(),
                booking.getBookingDates().getEndDate()
        );
        
        //calculate price using listing price_per_night
        //return base price for strategies to be used
        return listing.getPricePerNight().multiply(BigDecimal.valueOf(daysBetween));
        
    }
}
