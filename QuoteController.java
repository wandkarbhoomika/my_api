package com.project.api.controller;

import jakarta.validation.Valid;
import com.project.api.s3.S3Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.model.QuoteRequest;
import com.project.api.model.QuoteResponse;
import com.project.api.service.QuoteService;

@RestController
@RequestMapping("/api")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private S3Service s3Service;

    @PostMapping("/quote")
    public ResponseEntity<QuoteResponse> calculateAndSaveQuote(@RequestBody @Valid QuoteRequest request) {
        try {
            double monthlyPremium = quoteService.calculatePremium(request);
            String quoteId = "VE" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            LocalDate validTill = LocalDate.now().plusYears(1);

            // Prepare response
            QuoteResponse response = new QuoteResponse(
                    quoteId,
                    monthlyPremium,
                    validTill,
                    "SUCCESS",
                    "Quote calculated successfully"
            );

            // Save both request and response to S3
            String bucketName = System.getenv("BUCKET_NAME");
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> data = new HashMap<>();
            data.put("request", request);
            data.put("response", response);

            String json = mapper.writeValueAsString(data);
            s3Service.uploadToS3(bucketName, quoteId + ".json", json);

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

        } catch (Exception e) {
            QuoteResponse errorResponse = new QuoteResponse(
                    null,
                    0,
                    null,
                    "FAILED",
                    "Unexpected error: " + e.getMessage()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
