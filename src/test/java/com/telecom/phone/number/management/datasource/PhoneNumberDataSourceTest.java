package com.telecom.phone.number.management.datasource;

import com.telecom.phone.number.management.exception.NumberAlreadyActivatedException;
import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.model.PhoneNumber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link PhoneNumberDataSource}.
 */
class PhoneNumberDataSourceTest {

    private static final String STATIC_FIELD_NAME = "customerPhoneNumbers";

    @BeforeEach
    void setUp() throws Exception {
        // Clear and reinitialize static data using reflection
        Field field = PhoneNumberDataSource.class.getDeclaredField(STATIC_FIELD_NAME);
        field.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Long, List<PhoneNumber>> customerPhoneNumbers = (Map<Long, List<PhoneNumber>>) field.get(null);
        customerPhoneNumbers.clear();

        customerPhoneNumbers.put(1L, Arrays.asList(
            new PhoneNumber().number("1234567890").isActive(false),
            new PhoneNumber().number("9876543210").isActive(false)
        ));
        customerPhoneNumbers.put(2L, Collections.singletonList(
            new PhoneNumber().number("5555555555").isActive(false)
        ));
        customerPhoneNumbers.put(3L, Collections.emptyList());
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clear static data after tests
        Field field = PhoneNumberDataSource.class.getDeclaredField(STATIC_FIELD_NAME);
        field.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Long, List<PhoneNumber>> customerPhoneNumbers = (Map<Long, List<PhoneNumber>>) field.get(null);
        customerPhoneNumbers.clear();
    }

    @Test
    void testGetAllPhoneNumbers() {
        List<PhoneNumber> allNumbers = PhoneNumberDataSource.getAllPhoneNumbers();
        assertEquals(3, allNumbers.size(), "Expected 3 phone numbers across all customers");
    }

    @Test
    void testGetCustomerPhoneNumbers_ValidCustomer() {
        List<PhoneNumber> numbers = PhoneNumberDataSource.getCustomerPhoneNumbers(1L);
        assertEquals(2, numbers.size(), "Expected 2 phone numbers for customer ID 1");
    }

    @Test
    void testGetCustomerPhoneNumbers_InvalidCustomer() {
        assertThrows(ResourceNotFoundException.class, () ->
                PhoneNumberDataSource.getCustomerPhoneNumbers(999L),
            "Expected ResourceNotFoundException for non-existent customer ID"
        );
    }

    @Test
    void testActivatePhoneNumber_ValidNumber() {
        PhoneNumber activatedNumber = PhoneNumberDataSource.activatePhoneNumber(1L, "1234567890");
        assertTrue(activatedNumber.getIsActive(), "Expected phone number to be activated");
    }

    @Test
    void testActivatePhoneNumber_AlreadyActive() {
        PhoneNumberDataSource.activatePhoneNumber(1L, "1234567890");
        assertThrows(NumberAlreadyActivatedException.class, () ->
                PhoneNumberDataSource.activatePhoneNumber(1L, "1234567890"),
            "Expected NumberAlreadyActivatedException when activating an already active number"
        );
    }

    @Test
    void testActivatePhoneNumber_InvalidNumber() {
        assertThrows(ResourceNotFoundException.class, () ->
                PhoneNumberDataSource.activatePhoneNumber(1L, "0000000000"),
            "Expected ResourceNotFoundException for invalid phone number"
        );
    }

    @Test
    void testActivatePhoneNumber_InvalidCustomer() {
        assertThrows(ResourceNotFoundException.class, () ->
                PhoneNumberDataSource.activatePhoneNumber(999L, "1234567890"),
            "Expected ResourceNotFoundException for invalid customer ID"
        );
    }

    @Test
    void testGetCustomerPhoneNumbers_NoPhoneNumbers() {
        assertThrows(ResourceNotFoundException.class, () ->
                PhoneNumberDataSource.getCustomerPhoneNumbers(3L),
            "Expected ResourceNotFoundException for customer with no phone numbers"
        );
    }

    @Test
    void testActivatePhoneNumber_DuplicateNumbers() throws Exception {
        injectStaticData(Arrays.asList(
            new PhoneNumber().number("1234567890").isActive(false),
            new PhoneNumber().number("1234567890").isActive(false)
        ));

        PhoneNumber activatedNumber = PhoneNumberDataSource.activatePhoneNumber(1L, "1234567890");
        assertTrue(activatedNumber.getIsActive(), "Expected the first occurrence of the duplicate number to be activated");
    }

    @Test
    void testActivatePhoneNumber_CaseSensitivity() throws Exception {
        injectStaticData(Collections.singletonList(
            new PhoneNumber().number("1234567890").isActive(false)
        ));

        assertThrows(ResourceNotFoundException.class, () ->
                PhoneNumberDataSource.activatePhoneNumber(1L, "1234567890 "),
            "Expected ResourceNotFoundException for case or formatting differences"
        );
    }

    @Test
    void testActivatePhoneNumber_SpecialCharacters() throws Exception {
        injectStaticData(Collections.singletonList(
            new PhoneNumber().number("123-456-7890").isActive(false)
        ));

        assertThrows(ResourceNotFoundException.class, () ->
                PhoneNumberDataSource.activatePhoneNumber(1L, "1234567890"),
            "Expected ResourceNotFoundException for mismatched formatting"
        );
    }

    private void injectStaticData(List<PhoneNumber> phoneNumbers) throws Exception {
        Field field = PhoneNumberDataSource.class.getDeclaredField(STATIC_FIELD_NAME);
        field.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Long, List<PhoneNumber>> customerPhoneNumbers = (Map<Long, List<PhoneNumber>>) field.get(null);
        customerPhoneNumbers.put(1L, phoneNumbers);
    }
}
