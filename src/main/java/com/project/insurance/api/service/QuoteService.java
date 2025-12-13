package com.project.insurance.api.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.project.insurance.api.entity.QuoteHistory;
import com.project.insurance.api.model.QuoteRequest;
import com.project.insurance.api.model.QuoteResponse;
import com.project.insurance.api.repository.QuoteRepository;


@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private final Gson gson = new Gson();

    public double calculatePremium(QuoteRequest request) {
        int registrationYear = request.getRegistrationYear();
        int currentYear = java.time.LocalDate.now().getYear();

        if (registrationYear > currentYear) {
            throw new IllegalArgumentException("Incorrect registration year: cannot be in the future.");
        }

        if (request.getNumberOfWheels() < 2 || request.getNumberOfWheels() > 15) {
            throw new IllegalArgumentException("Invalid number of wheels: must be between 2 and 15.");
        }

        double basePremium = 1000;
        int vehicleAge = currentYear - registrationYear;
        double ageFactor = vehicleAge * 50;
        double wheelFactor = request.getNumberOfWheels() * 20;

        return basePremium + ageFactor + wheelFactor;
    }

    public void logQuote(QuoteRequest request, QuoteResponse response) {
        QuoteHistory history = new QuoteHistory();

        // Save request JSON
        history.setRequestPayload(gson.toJson(request));

        // Convert LocalDate to String for response JSON
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("quoteId", response.getQuoteId());
        responseMap.put("monthlyPremium", response.getMonthlyPremium());
        responseMap.put("validTill", response.getValidTill() == null ? null : response.getValidTill().toString());
        responseMap.put("status", response.getStatus());
        responseMap.put("message", response.getMessage());

        history.setResponsePayload(gson.toJson(responseMap));

        // Log request and response
        logger.info("Quote Request: {}", gson.toJson(request));
        logger.info("Quote Response: {}", gson.toJson(responseMap));

        // Copy other fields to entity
        history.setQuoteId(response.getQuoteId());
        history.setRegistrationYear(request.getRegistrationYear());
        history.setNumberOfWheels(request.getNumberOfWheels());
        history.setMonthlyPremium(response.getMonthlyPremium());
        history.setValidTill(response.getValidTill());
        history.setStatus(response.getStatus());
        history.setMessage(response.getMessage());

        // Save to DB
        quoteRepository.save(history);
    }
}
