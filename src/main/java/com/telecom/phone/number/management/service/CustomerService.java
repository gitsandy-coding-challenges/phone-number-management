package com.telecom.phone.number.management.service;

import com.telecom.phone.number.management.datasource.PhoneNumberDataSource;
import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.model.PhoneNumber;
import com.telecom.phone.number.management.model.PhoneNumbersResponse;
import com.telecom.phone.number.management.model.PhoneNumbersResponseLinks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class responsible for retrieving and activating phone numbers related to a customer.
 */
@Slf4j
@Service
public class CustomerService {

    /**
     * Retrieves all phone numbers associated with a specific customer.
     *
     * @param customerId the unique ID of the customer.
     * @return an {@link Optional} containing a {@link PhoneNumbersResponse} with the phone numbers and links.
    * */
    public Optional<PhoneNumbersResponse> getCustomerPhoneNumbers(Long customerId) {
        List<PhoneNumber> phoneNumbers = Optional.ofNullable(PhoneNumberDataSource.getCustomerPhoneNumbers(customerId))
            .filter(numbers -> !numbers.isEmpty())
            .orElseThrow(() -> {
                log.warn("No phone numbers found for customer ID: {}", customerId);
                return new ResourceNotFoundException("No phone numbers found for customer ID: " + customerId);
            });

        return Optional.of(createPhoneNumbersResponse(customerId, phoneNumbers, "GET", "/customers/{customerId}/phone-numbers"));
    }

    /**
     * Activates a specific phone number for a customer.
     *
     * @param customerId  the unique ID of the customer.
     * @param phoneNumber the phone number to activate.
     * @return an {@link Optional} containing a {@link PhoneNumbersResponse} with the activated phone number and links.
     */
    public Optional<PhoneNumbersResponse> activatePhoneNumber(Long customerId, String phoneNumber) {
        PhoneNumber activatedPhoneNumber = Optional.ofNullable(PhoneNumberDataSource.activatePhoneNumber(customerId, phoneNumber))
            .orElseThrow(() -> {
                log.warn("Activation failed: Phone number {} not found for customer ID: {}", phoneNumber, customerId);
                return new ResourceNotFoundException("Failed to activate phone number: " + phoneNumber);
            });

        return Optional.of(createPhoneNumbersResponse(
            customerId,
            List.of(activatedPhoneNumber),
            "PATCH",
            "/customers/{customerId}/phone-numbers/{phoneNumber}"));
    }

    /**
     * Creates a {@link PhoneNumbersResponse} for the given customer ID, phone numbers, HTTP method, and path.
     *
     * @param customerId   the unique ID of the customer.
     * @param phoneNumbers the list of phone numbers to include in the response.
     * @param httpMethod   the HTTP method associated with the link.
     * @param path         the path template for the link.
     * @return a {@link PhoneNumbersResponse} containing phone numbers and links.
     */
    PhoneNumbersResponse createPhoneNumbersResponse(Long customerId, List<PhoneNumber> phoneNumbers, String httpMethod, String path) {
        // Generate top-level links
        String link = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path(path)
            .buildAndExpand(customerId, phoneNumbers.stream()
                .map(PhoneNumber::getNumber)
                .collect(Collectors.joining(","))) // Join numbers for link if needed
            .toUriString();

        PhoneNumbersResponseLinks links = new PhoneNumbersResponseLinks()
            .self(new com.telecom.phone.number.management.model.Link()
                .href(link)
                .httpMethod(httpMethod)
                .templated(false));

        // Create and return the response
        return new PhoneNumbersResponse()
            .phoneNumbers(phoneNumbers)
            .links(links);
    }
}
