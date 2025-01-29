package com.telecom.phone.number.management.service;

import com.telecom.phone.number.management.datasource.PhoneNumberDataSource;
import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.model.Link;
import com.telecom.phone.number.management.model.PhoneNumber;
import com.telecom.phone.number.management.model.PhoneNumbersResponse;
import com.telecom.phone.number.management.model.PhoneNumbersResponseLinks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

/**
 * Service class responsible for managing phone number operations.
 * This service handles the retrieval of all phone numbers and constructing the response.
 */
@Slf4j
@Service
public class PhoneNumberService {

    /**
     * Retrieves all phone numbers from the data source and constructs a response with the phone numbers
     * along with associated metadata and links.
     *
     * @return a {@link PhoneNumbersResponse} containing all phone numbers and their links.
     * @throws ResourceNotFoundException if no phone numbers are found in the system.
     */
    public PhoneNumbersResponse getAllPhoneNumbers() {
        log.debug("Fetching all phone numbers from the data source.");
        List<PhoneNumber> allPhoneNumbers = PhoneNumberDataSource.getAllPhoneNumbers();

        // Create the top-level self link
        String topLevelLink = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/phone-numbers")
            .toUriString();

        PhoneNumbersResponseLinks responseLinks = new PhoneNumbersResponseLinks()
            .self(new Link().href(topLevelLink).httpMethod("GET").templated(false));

        return new PhoneNumbersResponse()
            .phoneNumbers(allPhoneNumbers)
            .links(responseLinks);
    }
}
