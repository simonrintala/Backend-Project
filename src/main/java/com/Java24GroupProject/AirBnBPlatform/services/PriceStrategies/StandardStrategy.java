package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import java.math.BigDecimal;

public class StandardStrategy implements PriceStrategyService {
    @Override
    public void calculatePrice(BigDecimal price) {
        // returns the normal price. should it be built like the others and multiply by 1 or just overkill?
        System.out.println("StandardStrategy calculation " + price);
    }
}