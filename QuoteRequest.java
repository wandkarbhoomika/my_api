package com.project.api.model;

import jakarta.validation.constraints.NotNull;

public class QuoteRequest {
	@NotNull
	private int registrationYear;

	@NotNull
	private int numberOfWheels;

	public int getRegistrationYear() {
		return registrationYear;
	}

	public void setRegistrationYear(int registrationYear) {
		this.registrationYear = registrationYear;
	}

	public int getNumberOfWheels() {
		return numberOfWheels;
	}

	public void setNumberOfWheels(int numberOfWheels) {
		this.numberOfWheels = numberOfWheels;
	}

	public QuoteRequest() {
	}

	public QuoteRequest(int registrationYear, int numberOfWheels) {
		this.registrationYear = registrationYear;
		this.numberOfWheels = numberOfWheels;
	}
}
