package com.telecom.phone.number.management.controller;

import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.model.PhoneNumber;
import com.telecom.phone.number.management.model.PhoneNumbersResponse;
import com.telecom.phone.number.management.service.PhoneNumberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PhoneNumberController}.
 */
@ExtendWith(MockitoExtension.class)
public class PhoneNumberControllerTest {

    @Mock
    private PhoneNumberService phoneNumberService;

    @InjectMocks
    private PhoneNumberController phoneNumberController;

    private PhoneNumbersResponse phoneNumbersResponse;

    @BeforeEach
    void setUp() {
        // Initialize a PhoneNumbersResponse with some mock data
        phoneNumbersResponse = new PhoneNumbersResponse();
    }

    @Test
    void testGetAllPhoneNumbersSuccess() {
        phoneNumbersResponse.setPhoneNumbers(Arrays.asList(new PhoneNumber(), new PhoneNumber()));
        when(phoneNumberService.getAllPhoneNumbers()).thenReturn(phoneNumbersResponse);

        ResponseEntity<PhoneNumbersResponse> response = phoneNumberController.getAllPhoneNumbers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, Objects.requireNonNull(response.getBody()).getPhoneNumbers().size());
    }

    @Test
    void testGetAllPhoneNumbersServiceException() {
        when(phoneNumberService.getAllPhoneNumbers()).thenThrow(new ResourceNotFoundException("No numbers exist in the system"));

        try {
            phoneNumberController.getAllPhoneNumbers();
        } catch (ResourceNotFoundException e) {
            assertEquals("No numbers exist in the system", e.getMessage());
        }
    }
}
