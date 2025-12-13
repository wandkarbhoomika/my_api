package com.project.insurance.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.insurance.api.model.QuoteRequest;
import com.project.insurance.api.model.QuoteResponse;
import com.project.insurance.api.service.QuoteService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
public class QuoteController {

	private final QuoteService quoteService;

	public QuoteController(QuoteService quoteService) {
		this.quoteService = quoteService;
	}

	@PostMapping("/quote")
	public ResponseEntity<QuoteResponse> calculateAndSaveQuote(@Valid @RequestBody QuoteRequest request) {
		try {
			double premium = quoteService.calculatePremium(request);
			String quoteId = "VE" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
			LocalDate validTill = LocalDate.now().plusYears(1);

			QuoteResponse response = new QuoteResponse(quoteId, premium, validTill, "SUCCESS",
					"Quote calculated successfully");

			quoteService.logQuote(request, response);
			return ResponseEntity.ok(response);

		} catch (IllegalArgumentException e) {
			QuoteResponse errorResponse = new QuoteResponse(null, 0, null, "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		} catch (Exception e) {
			QuoteResponse errorResponse = new QuoteResponse(null, 0, null, "FAILED",
					"Unexpected error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

}
