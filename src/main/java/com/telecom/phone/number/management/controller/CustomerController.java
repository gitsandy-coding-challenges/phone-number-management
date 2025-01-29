package com.telecom.phone.number.management.controller;

import com.telecom.phone.number.management.api.CustomersApi;
import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.model.PhoneNumbersResponse;
import com.telecom.phone.number.management.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing customer phone numbers.
 * This class handles requests related to customer phone numbers, such as fetching and activating numbers.
 * Implements the {@link CustomersApi} interface for API specifications.
 *
 * @author Sandeep
 * @version 1.0
 * @since 2025-01-27
 */
@Slf4j
@RestController
public class CustomerController implements CustomersApi {

    @Autowired
    CustomerService service;

    /**
     * Fetches all phone numbers associated with a specific customer.
     *
     * @param customerId the unique ID of the customer.
     * @return a {@link ResponseEntity} containing a {@link PhoneNumbersResponse} with the customer's phone numbers,
     *         or throws a {@link ResourceNotFoundException} if the customer does not exist.
     */
    @Override
    public ResponseEntity<PhoneNumbersResponse> getCustomerPhoneNumbers(Long customerId) {
        log.info("Received request to fetch phone numbers for customer ID: {}", customerId);
        return service.getCustomerPhoneNumbers(customerId)
            .map(response -> {
                log.info("Successfully retrieved phone numbers for customer ID: {}", customerId);
                return ResponseEntity.ok(response);
            })
            .orElseThrow(() -> {
                log.error("Customer not found with ID: {}", customerId);
                return new ResourceNotFoundException("Customer not found with ID: " + customerId);
            });
    }

    /**
     * Activates a specific phone number for a customer.
     *
     * @param customerId  the unique ID of the customer.
     * @param phoneNumber the phone number to activate.
     * @return a {@link ResponseEntity} containing a {@link PhoneNumbersResponse} with the updated phone number details,
     *         or throws a {@link ResourceNotFoundException} if the customer or phone number does not exist.
     */
    @Override
    public ResponseEntity<PhoneNumbersResponse> activatePhoneNumber(Long customerId, String phoneNumber) {
        log.info("Received request to activate phone number {} for customer ID: {}", phoneNumber, customerId);
        return service.activatePhoneNumber(customerId, phoneNumber)
            .map(response -> {
                log.info("Successfully activated phone number {} for customer ID: {}", phoneNumber, customerId);
                return ResponseEntity.ok(response);
            })
            .orElseThrow(() -> {
                log.error("Customer not found with ID: {} or phone number: {} does not exist", customerId, phoneNumber);
                return new ResourceNotFoundException("Customer not found with ID: " + customerId);
            });
    }
}

