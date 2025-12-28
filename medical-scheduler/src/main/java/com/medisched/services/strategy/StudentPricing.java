package com.medisched.services.strategy;

import org.springframework.stereotype.Component;

@Component("student")
public class StudentPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * 0.5; // 50% reducere
    }

    @Override
    public String getName() {
        return "Student";
    }
}
