package com.telecom.phone.number.management.service;

import com.telecom.phone.number.management.datasource.PhoneNumberDataSource;
import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.model.PhoneNumber;
import com.telecom.phone.number.management.model.PhoneNumbersResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

// Mockito specific imports
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;

// Assertions imports
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Unit tests for {@link PhoneNumberService}.
 */
class PhoneNumberServiceTest {

    private PhoneNumberService phoneNumberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        phoneNumberService = new PhoneNumberService();
    }

    @Test
    void testGetAllPhoneNumbers_Success() {
        // Setup mock data
        List<PhoneNumber> mockPhoneNumbers = List.of(
            new PhoneNumber().number("1234567890").isActive(true),
            new PhoneNumber().number("9876543210").isActive(false)
        );

        // Mock static method for PhoneNumberDataSource.getAllPhoneNumbers()
        try (MockedStatic<PhoneNumberDataSource> mockedDataSource = mockStatic(PhoneNumberDataSource.class)) {
            mockedDataSource.when(PhoneNumberDataSource::getAllPhoneNumbers)
                .thenReturn(mockPhoneNumbers);

            // Mock ServletUriComponentsBuilder
            try (MockedStatic<ServletUriComponentsBuilder> mockedUriBuilder = mockStatic(ServletUriComponentsBuilder.class)) {
                ServletUriComponentsBuilder mockUriBuilder = mock(ServletUriComponentsBuilder.class);
                mockedUriBuilder.when(ServletUriComponentsBuilder::fromCurrentContextPath)
                    .thenReturn(mockUriBuilder);
                when(mockUriBuilder.path(anyString())).thenReturn(mockUriBuilder);
                when(mockUriBuilder.toUriString()).thenReturn("http://localhost/phone-numbers");

                // Act
                PhoneNumbersResponse response = phoneNumberService.getAllPhoneNumbers();

                // Assert
                assertNotNull(response);
                assertEquals(2, response.getPhoneNumbers().size());
                assertEquals("1234567890", response.getPhoneNumbers().get(0).getNumber());
                assertEquals("9876543210", response.getPhoneNumbers().get(1).getNumber());
            }
        }
    }

    @Test
    void testGetAllPhoneNumbers_ResourceNotFoundException() {
        // Simulate throwing exception when the phone numbers list is empty
        try (MockedStatic<PhoneNumberDataSource> mockedDataSource = mockStatic(PhoneNumberDataSource.class)) {
            mockedDataSource.when(PhoneNumberDataSource::getAllPhoneNumbers)
                .thenThrow(new ResourceNotFoundException("No numbers found"));

            // Act & Assert
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> phoneNumberService.getAllPhoneNumbers());

            assertEquals("No numbers found", exception.getMessage());
        }
    }
}
