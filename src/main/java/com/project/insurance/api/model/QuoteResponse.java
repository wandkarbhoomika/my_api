package com.project.insurance.api.model;

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

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public double getMonthlyPremium() {
		return monthlyPremium;
	}

	public void setMonthlyPremium(double monthlyPremium) {
		this.monthlyPremium = monthlyPremium;
	}

	public LocalDate getValidTill() {
		return validTill;
	}

	public void setValidTill(LocalDate validTill) {
		this.validTill = validTill;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
