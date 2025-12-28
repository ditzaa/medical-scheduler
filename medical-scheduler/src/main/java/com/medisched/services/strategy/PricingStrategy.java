package com.medisched.services.strategy;

public interface PricingStrategy {
    double calculatePrice(double basePrice);
    String getName();
}
