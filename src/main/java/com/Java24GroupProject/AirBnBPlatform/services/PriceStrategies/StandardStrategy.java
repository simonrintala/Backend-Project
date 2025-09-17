package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import java.math.BigDecimal;

public class StandardStrategy implements PriceStrategyService {
    
        @Override
        public BigDecimal calculatePrice(BigDecimal price) {
            // returns the normal price.
            return price;
        }
}