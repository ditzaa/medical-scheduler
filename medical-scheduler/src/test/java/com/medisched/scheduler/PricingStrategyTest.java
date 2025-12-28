package com.medisched.scheduler;

import com.medisched.services.strategy.PricingStrategy;
import com.medisched.services.strategy.InsurancePricing;
import com.medisched.services.strategy.StudentPricing;
import com.medisched.services.strategy.StandardPricing;
import com.medisched.services.strategy.EmergencyPricing;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PricingStrategyTest {

    @Test
    void testPricingStrategies() {
        double basePrice = 200.0;
        
        PricingStrategy standard = new StandardPricing();
        PricingStrategy insurance = new InsurancePricing();
        PricingStrategy student = new StudentPricing();
        PricingStrategy emergency = new EmergencyPricing();

        assertEquals(200.0, standard.calculatePrice(basePrice));
        assertEquals(0.0, insurance.calculatePrice(basePrice));
        assertEquals(100.0, student.calculatePrice(basePrice));
        assertEquals(240.0, emergency.calculatePrice(basePrice));
    }
}
