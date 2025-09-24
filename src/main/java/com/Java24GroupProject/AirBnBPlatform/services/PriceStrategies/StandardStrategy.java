package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import java.math.BigDecimal;

public class StandardStrategy implements IPriceStrategy {
    
        @Override
        public BigDecimal calculatePrice(BigDecimal price) {
            // returns the normal price.
            return price;
        }
}