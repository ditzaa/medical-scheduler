package com.medisched.services.strategy;

import org.springframework.stereotype.Component;

@Component("standard")
public class StandardPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice;
    }

    @Override
    public String getName() {
        return "Standard";
    }
}
