package com.project.api.model;

import java.time.LocalDate;

public class QuoteResponse {
    private String quoteId;
    private double monthlyPremium;
    private LocalDate validTill;
    private String status;
    private String message;

    public QuoteResponse(String quoteId, double monthlyPremium, LocalDate validTill, String status, String message) {
        this.quoteId = quoteId;
        this.monthlyPremium = monthlyPremium;
        this.validTill = validTill;
        this.status = status;
        this.message = message;
    }

    // Getters (Jackson needs getters to serialize)
    public String getQuoteId() { return quoteId; }
    public double getMonthlyPremium() { return monthlyPremium; }
    public LocalDate getValidTill() { return validTill; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
