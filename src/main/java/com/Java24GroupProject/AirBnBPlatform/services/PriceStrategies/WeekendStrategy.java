package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import java.math.BigDecimal;

public class WeekendStrategy implements IPriceStrategy {
    // create multiplier for weekend pricing
    private final BigDecimal multiplier = new BigDecimal("1.5");
    
    @Override
    public BigDecimal calculatePrice(BigDecimal price) {
        // multiply the listing price by weekend pricing then return it.
        price = price.multiply(multiplier);
        return price;
    }
}
