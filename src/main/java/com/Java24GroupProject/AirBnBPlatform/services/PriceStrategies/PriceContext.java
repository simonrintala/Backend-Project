package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import com.Java24GroupProject.AirBnBPlatform.models.Booking;
import com.Java24GroupProject.AirBnBPlatform.models.Listing;

import java.math.BigDecimal;

public class PriceContext {
    private PriceStrategyService strategy;
    private final PriceCalculation priceCalculation = new PriceCalculation();

    
    
    public PriceContext(PriceStrategyService strategy) {
        this.strategy = strategy;
        
    }
    
    // swaps strategy when needed.
    public void changeStrategy(PriceStrategyService strategy) {
        this.strategy = strategy;
    }
    
    // run price through strategies and saves new price.
    public void runCalculation(Booking booking, Listing listing) {
        BigDecimal newPrice = strategy.calculatePrice(priceCalculation.calculateAndSetPrice(booking, listing));
        booking.setTotalPrice(newPrice);
    }
}
