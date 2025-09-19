package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;


import java.math.BigDecimal;

public class HolidayStrategy implements PriceStrategyService {
    // create multiplier for holiday pricing
    private final BigDecimal multiplier = new BigDecimal("1.25");
    
    // holiday strategy is done for later usage when the rest of the code is implemented.
    @Override
    public BigDecimal calculatePrice(BigDecimal price) {
        // // multiply the listing price by holiday pricing then return it.
        price = price.multiply(multiplier);
        return price;
    }
}
