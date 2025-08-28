package com.project.api.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.api.model.QuoteRequest;
import com.project.api.model.QuoteResponse;
import com.project.api.service.QuoteService;

@RestController
@RequestMapping("/api")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @PostMapping(value = "/quote", produces = "application/json")
    public ResponseEntity<QuoteResponse> calculateQuote(@RequestBody @Valid QuoteRequest request) {
        try {
            double monthlyPremium = quoteService.calculatePremium(request);
            String quoteId = "VE" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            LocalDate validTill = LocalDate.now().plusYears(1);

            QuoteResponse response = new QuoteResponse(
                quoteId,
                monthlyPremium,
                validTill,
                "SUCCESS",
                "Quote calculated successfully"
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            QuoteResponse errorResponse = new QuoteResponse(
                null,
                0,
                null,
                "FAILED",
                e.getMessage()
            );

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}