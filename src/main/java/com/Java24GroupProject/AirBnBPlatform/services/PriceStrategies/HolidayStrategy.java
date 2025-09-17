package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;


import java.math.BigDecimal;

public class HolidayStrategy implements PriceStrategyService {
    // create multiplier for holiday pricing
    private final BigDecimal multiplier = new BigDecimal("1.25");
    
    @Override
    public void calculatePrice(BigDecimal price) {
        // // multiply the listing price by holiday pricing
        BigDecimal totalPrice = price.multiply(multiplier);
        System.out.println("Holiday Strategy calculation " + totalPrice);
    }
}
