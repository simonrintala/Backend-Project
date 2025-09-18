package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * The PriceContext class is used to change between price strategies
 * and also run the calculation with the correct strategy to provide
 * the total price.
 */

public class PriceContext {
    private PriceStrategyService strategy;
    private final PriceCalculation priceCalculation = new PriceCalculation();

    
    
    public PriceContext(PriceStrategyService strategy) {
        this.strategy = strategy;
    }
    
    // swaps strategy when needed.
    private void changeStrategy(PriceStrategyService strategy) {
        this.strategy = strategy;
    }
    
    // run price through strategies and saves new price.
    public void runCalculation(Booking booking, Listing listing) {
        
        boolean hasWeekend = priceCalculation.hasWeekend(booking);

        // if hasWeekend true, Weekend strategy, else run standard strategy
        if (hasWeekend) {
            changeStrategy(new WeekendStrategy());
        } else {
            changeStrategy(new StandardStrategy());
        }
        // Holiday strategy is yet to be implemented due to lack of time
        // but will have its own changeStrategy once it's done.
        
        // calculate the new price with the set Strategy
        BigDecimal newPrice = strategy.calculatePrice(priceCalculation.calculateAndSetPrice(booking, listing));
        booking.setTotalPrice(newPrice);
    }
    

}
