package com.Java24GroupProject.AirBnBPlatform.services.PriceStrategies;

public class PriceContext {
    private PriceStrategyService strategy;
    private final PriceCalculation pricesCalc = new PriceCalculation();
    
    
    public PriceContext(PriceStrategyService strategy) {
        this.strategy = strategy;
        
    }
    
    public void changeStrategy(PriceStrategyService strategy) {
        this.strategy = strategy;
    }
    
    public void runCalculation() {
        strategy.calculatePrice(pricesCalc.getCalculatedPrice(listing, booking));
    }
}
