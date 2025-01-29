package com.telecom.phone.number.management.controller;

import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.model.PhoneNumbersResponse;
import com.telecom.phone.number.management.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

/**
 * Unit tests for {@link CustomerController}.
 */
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private static final Long CUSTOMER_ID = 123L;
    private static final String PHONE_NUMBER = "555-5555";
    private PhoneNumbersResponse phoneNumbersResponse;

    @BeforeEach
    void setUp() {
        phoneNumbersResponse = new PhoneNumbersResponse();
    }

    @Test
    void testGetCustomerPhoneNumbers_Success() {
        when(customerService.getCustomerPhoneNumbers(CUSTOMER_ID)).thenReturn(Optional.of(phoneNumbersResponse));

        ResponseEntity<PhoneNumbersResponse> response = customerController.getCustomerPhoneNumbers(CUSTOMER_ID);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(phoneNumbersResponse, response.getBody());
    }

    @Test
    void testGetCustomerPhoneNumbers_CustomerNotFound() {
        when(customerService.getCustomerPhoneNumbers(CUSTOMER_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            customerController.getCustomerPhoneNumbers(CUSTOMER_ID));

        assertEquals("Customer not found with ID: " + CUSTOMER_ID, exception.getMessage());
    }

    @Test
    void testActivatePhoneNumber_Success() {
        when(customerService.activatePhoneNumber(CUSTOMER_ID, PHONE_NUMBER)).thenReturn(Optional.of(phoneNumbersResponse));

        ResponseEntity<PhoneNumbersResponse> response = customerController.activatePhoneNumber(CUSTOMER_ID, PHONE_NUMBER);

        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertEquals(phoneNumbersResponse, response.getBody());
    }

    @Test
    void testActivatePhoneNumber_CustomerOrPhoneNumberNotFound() {
        when(customerService.activatePhoneNumber(CUSTOMER_ID, PHONE_NUMBER)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            customerController.activatePhoneNumber(CUSTOMER_ID, PHONE_NUMBER));

        assertEquals("Customer not found with ID: " + CUSTOMER_ID, exception.getMessage());
    }
}
