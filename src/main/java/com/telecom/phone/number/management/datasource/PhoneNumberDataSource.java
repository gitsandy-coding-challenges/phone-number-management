package com.telecom.phone.number.management.datasource;

import com.telecom.phone.number.management.exception.NumberAlreadyActivatedException;
import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.model.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * A data source class that simulates a datastore for managing customer phone numbers.
 *
 * <p>This class contains static data to represent customers and their associated phone numbers.
 * It provides methods to retrieve phone numbers, activate phone numbers, and fetch phone numbers
 * for a specific customer. The data is pre-initialized for demonstration purposes.</p>
 *
 * <p>Note: This implementation is not thread-safe and is meant for simulation only.</p>
 *
 * @author Sandeep
 * @version 1.0
 * @since 2025-01-27
 */
public class PhoneNumberDataSource {

    private static final Logger log = LoggerFactory.getLogger(PhoneNumberDataSource.class);


    // Static data structure to simulate a datastore
    private static final Map<Long, List<PhoneNumber>> customerPhoneNumbers = new HashMap<>();

    static {
        // Initialize with sample data
        customerPhoneNumbers.put(1L, Arrays.asList(
            new PhoneNumber().number("1234567890").isActive(false),
            new PhoneNumber().number("9876543210").isActive(false)
        ));
        customerPhoneNumbers.put(2L, Collections.singletonList(
            new PhoneNumber().number("5555555555").isActive(false)
        ));
        customerPhoneNumbers.put(3L, new ArrayList<>()); // Customer with no phone numbers
    }

    /**
     * Retrieves all phone numbers from all customers in the datastore.
     * @return A list of all phone numbers across all customers.
     */
    public static List<PhoneNumber> getAllPhoneNumbers() {
        List<PhoneNumber> allPhoneNumbers = Optional.of(customerPhoneNumbers)
            .map(Map::values)
            .orElse(Collections.emptyList())
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        // If no phone numbers are found, throw ResourceNotFoundException
        if (allPhoneNumbers.isEmpty()) {
            throw new ResourceNotFoundException("No numbers exist in the system");
        }

        return allPhoneNumbers;
    }

    /**
     * Retrieves phone numbers for a specific customer.
     *
     * @param customerId The ID of the customer whose phone numbers are to be retrieved.
     * @return A list of phone numbers associated with the specified customer.
     * @throws ResourceNotFoundException If the customer does not exist or has no phone numbers.
     */
    public static List<PhoneNumber> getCustomerPhoneNumbers(Long customerId) {
        log.debug("Fetching phone numbers for customer ID: {}", customerId);
        return getPhoneNumbersForCustomer(customerId);
    }

    /**
     * Activates a phone number for a specific customer.
     *
     * <p>This method validates the existence of the customer and phone number before activation.
     * If the phone number is already active, an exception is thrown.</p>
     *
     * @param customerId The ID of the customer.
     * @param phoneNumber The phone number to activate.
     * @return The activated phone number.
     * @throws ResourceNotFoundException If the customer or phone number does not exist.
     * @throws NumberAlreadyActivatedException If the phone number is already active.
     */
    public static PhoneNumber activatePhoneNumber(Long customerId, String phoneNumber) {

        log.debug("Activating phone number {} for customer ID: {}", phoneNumber, customerId);
        List<PhoneNumber> phoneNumbers = getPhoneNumbersForCustomer(customerId);

        // Find the phone number to activate
        return phoneNumbers.stream()
            .filter(customerPhoneNumber -> customerPhoneNumber.getNumber().equals(phoneNumber))
            .findFirst()
            .map(numberToBeActivated -> {
                if (numberToBeActivated.getIsActive()) {
                    log.warn("Phone number {} is already active for customer ID: {}", phoneNumber, customerId);
                    throw new NumberAlreadyActivatedException("Phone number " + phoneNumber + " is already active.");
                } else {
                    numberToBeActivated.setIsActive(true);
                }
                return numberToBeActivated;
            })
            .orElseThrow(() -> {
                log.error("Phone number {} not found for customer ID: {}", phoneNumber, customerId);
                return new ResourceNotFoundException("Phone number not found for customer ID: " + customerId);
            });
    }

    /**
     * Retrieves phone numbers for a customer after validating the customer's existence.
     *
     * @param customerId The ID of the customer.
     * @return A list of phone numbers associated with the specified customer.
     * @throws ResourceNotFoundException If the customer does not exist or has no phone numbers.
     */
    private static List<PhoneNumber> getPhoneNumbersForCustomer(Long customerId) {
        log.debug("Validating existence of customer ID: {}", customerId);
        return Optional.ofNullable(customerPhoneNumbers.get(customerId))
            .map(phoneNumbers -> {
                if (phoneNumbers.isEmpty()) {
                    log.warn("No phone numbers found for customer ID: {}", customerId);
                    throw new ResourceNotFoundException("No phone numbers found for customer: " + customerId);
                }
                return phoneNumbers;
            })
            .orElseThrow(() -> {
                log.error("Customer ID {} not found in the data source.", customerId);
                return new ResourceNotFoundException("Customer not found with ID: " + customerId);
            });
    }
}
