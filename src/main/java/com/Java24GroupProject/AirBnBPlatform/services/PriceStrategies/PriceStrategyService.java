package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

import java.math.BigDecimal;

/**
 * Class for handling price calculations etc.
 * To be built upon into a strategy pattern or such
 *
 * FunctionalInterface to make sure there can't be more than one abstract method.
 * This class is here to provide the same object to different classes
 * to later apply their own custom implementation for the pricing.
 *
 */
@FunctionalInterface
public interface PriceStrategyService {
    BigDecimal calculatePrice(BigDecimal price);
}
