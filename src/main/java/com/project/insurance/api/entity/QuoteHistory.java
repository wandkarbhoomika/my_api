package com.project.insurance.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "quote_history") // table name  DB
public class QuoteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quote_id")
    private String quoteId;

    @Column(name = "requestPayload", columnDefinition = "TEXT") 
    @Lob
    private String requestPayload;

    @Lob
    @Column(name = "responsePayload", columnDefinition = "TEXT")
    private String responsePayload;

    @Column(name = "registration_year")
    private Integer registrationYear;

    @Column(name = "number_of_wheels")
    private Integer numberOfWheels;

    @Column(name = "premium")
    private Double monthlyPremium;

    @Column(name = "valid_till")
    private LocalDate validTill;

    private String status;
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public String getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}

	public String getResponsePayload() {
		return responsePayload;
	}

	public void setResponsePayload(String responsePayload) {
		this.responsePayload = responsePayload;
	}

	public Integer getRegistrationYear() {
		return registrationYear;
	}

	public void setRegistrationYear(Integer registrationYear) {
		this.registrationYear = registrationYear;
	}

	public Integer getNumberOfWheels() {
		return numberOfWheels;
	}

	public void setNumberOfWheels(Integer numberOfWheels) {
		this.numberOfWheels = numberOfWheels;
	}

	public Double getMonthlyPremium() {
		return monthlyPremium;
	}

	public void setMonthlyPremium(Double monthlyPremium) {
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
