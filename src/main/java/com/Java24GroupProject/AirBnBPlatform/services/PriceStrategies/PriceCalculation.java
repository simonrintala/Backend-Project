package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.DayOfWeek;

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
    
    public boolean hasWeekend(Booking booking) {
        LocalDate startDate = booking.getBookingDates().getStartDate();
        LocalDate endDate = booking.getBookingDates().getEndDate();
        
        // loop for as long as startDate is before endDate, plus 1 day each iteration
        for(LocalDate day = startDate; day.isBefore(endDate); day = day.plusDays(1)) {
            //get current day and save it
            DayOfWeek dayOfWeek = day.getDayOfWeek();
            
            //check if current day is a weekend, if so return true
            if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                return  true;
            }
        }
        return false;
    }
    
}
