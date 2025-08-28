package com.project.api.service;

import org.springframework.stereotype.Service;
import com.project.api.model.QuoteRequest;

@Service
public class QuoteService {
    // business rule
	public double calculatePremium(QuoteRequest request) {
        int registrationYear = request.getRegistrationYear();
        int currentYear = java.time.LocalDate.now().getYear();
        
        // Validation: registration year should not be in the future
        if (request.getRegistrationYear() > currentYear) {
            throw new IllegalArgumentException("Incorrect registration year: cannot be in the future.");
        }

        // Validation: number of wheels should be <= 15
        if (request.getNumberOfWheels() > 12) {
            throw new IllegalArgumentException("Invalid number of wheels: cannot be more than 15.");
        }
        double basePremium = 1000;
        int vehicleAge = currentYear - registrationYear;
        double ageFactor = vehicleAge * 50;
        double wheelFactor = request.getNumberOfWheels() * 20;

        return basePremium + ageFactor + wheelFactor;
    }
}

