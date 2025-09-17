package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import java.math.BigDecimal;

public class WeekendStrategy implements PriceStrategyService {
    // create multiplier for weekend pricing
    private final BigDecimal multiplier = new BigDecimal("1.5");
    
    @Override
    public void calculatePrice(BigDecimal price) {
        // multiply the listing price by weekend pricing
        BigDecimal totalPrice = price.multiply(multiplier);
        System.out.println("Weekend Strategy calculation " + totalPrice);
    }
}
