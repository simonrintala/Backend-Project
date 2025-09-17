package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import java.math.BigDecimal;

/**
 * Class for handling price calculations etc.
 * To be built upon into a strategy pattern or such
 */

public interface PriceStrategyService {
    BigDecimal calculatePrice(BigDecimal price);
}
