package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class PriceCalculation {
    
    
    
    PriceCalculation() {
    }
    
    
    public BigDecimal getCalculatedPrice(Listing listing, Booking booking) {
        return calculateAndSetPrice(booking, listing);
    }
    
    
    //calculate price from nr of booked days and price per night from listing
    private BigDecimal calculateAndSetPrice(Booking booking, Listing listing) {
        //calculate days in between start and end date
        long daysBetween = ChronoUnit.DAYS.between(
                booking.getBookingDates().getStartDate(),
                booking.getBookingDates().getEndDate()
        );
        
        //calculate price using listing price_per_night
        BigDecimal totalPrice = listing.getPricePerNight().multiply(BigDecimal.valueOf(daysBetween));
        
        //set total price of booking
        booking.setTotalPrice(totalPrice);
        return totalPrice;
    }
}
