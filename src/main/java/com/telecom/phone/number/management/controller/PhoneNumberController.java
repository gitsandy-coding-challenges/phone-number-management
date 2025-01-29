package com.telecom.phone.number.management.controller;

import com.telecom.phone.number.management.api.PhoneNumbersApi;
import com.telecom.phone.number.management.model.PhoneNumbersResponse;
import com.telecom.phone.number.management.service.PhoneNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing phone numbers.
 * Handles operations such as retrieving all phone numbers available in the system.
 * Implements the {@link PhoneNumbersApi} interface for API specifications.
 *
 * @author Sandeep
 * @version 1.0
 * @since 2025-01-27
 */
@Slf4j
@RestController
public class PhoneNumberController implements PhoneNumbersApi {

    @Autowired
    private PhoneNumberService service;

    /**
     * Retrieves all phone numbers available in the system.
     *
     * @return a {@link ResponseEntity} containing a {@link PhoneNumbersResponse} with the list of all phone numbers.
     */
    @Override
    public ResponseEntity<PhoneNumbersResponse> getAllPhoneNumbers() {
        log.info("Received request to fetch all phone numbers.");
        PhoneNumbersResponse response = service.getAllPhoneNumbers();
        log.info("Successfully retrieved {} phone numbers.", response.getPhoneNumbers().size());
        return ResponseEntity.ok(response);
    }
}
