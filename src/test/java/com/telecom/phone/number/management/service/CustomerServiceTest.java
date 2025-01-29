package com.telecom.phone.number.management.service;

import com.telecom.phone.number.management.datasource.PhoneNumberDataSource;
import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.model.PhoneNumber;
import com.telecom.phone.number.management.model.PhoneNumbersResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link CustomerService}.
 */
class CustomerServiceTest {

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService();
    }

    @Test
    void testGetCustomerPhoneNumbers_Success() {
        // Setup mock data
        List<PhoneNumber> mockPhoneNumbers = List.of(
            new PhoneNumber().number("1234567890").isActive(true),
            new PhoneNumber().number("9876543210").isActive(false)
        );

        // Mock static method for PhoneNumberDataSource.getCustomerPhoneNumbers()
        try (MockedStatic<PhoneNumberDataSource> mockedDataSource = mockStatic(PhoneNumberDataSource.class)) {
            mockedDataSource.when(() -> PhoneNumberDataSource.getCustomerPhoneNumbers(1L))
                .thenReturn(mockPhoneNumbers);

            // Set the request context for ServletUriComponentsBuilder
            MockHttpServletRequest mockRequest = new MockHttpServletRequest();
            mockRequest.setContextPath("/api/v1");
            mockRequest.setMethod("GET");
            mockRequest.setRequestURI("/customers/1/phone-numbers");
            mockRequest.setServerName("localhost");
            mockRequest.setServerPort(8080);

            ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
            RequestContextHolder.setRequestAttributes(attributes);

            // Act
            PhoneNumbersResponse response = customerService.getCustomerPhoneNumbers(1L).get();

            // Assert
            assertNotNull(response);
            assertEquals(2, response.getPhoneNumbers().size());
            assertEquals(mockPhoneNumbers.getFirst().getNumber(), response.getPhoneNumbers().getFirst().getNumber());
            assertEquals(mockPhoneNumbers.getFirst().getIsActive(), response.getPhoneNumbers().getFirst().getIsActive());

            // Verify the generated URL in the links section
            assertEquals("http://localhost:8080/api/v1/customers/1/phone-numbers", response.getLinks().getSelf().getHref());
        }
    }


    @Test
    void testGetCustomerPhoneNumbers_ResourceNotFoundException() {
        // Simulate throwing exception when the phone numbers list is empty for customerId 5L
        try (MockedStatic<PhoneNumberDataSource> mockedDataSource = mockStatic(PhoneNumberDataSource.class)) {

            // Mocking the static method call to simulate the exception when no phone numbers are found
            mockedDataSource.when(() -> PhoneNumberDataSource.getCustomerPhoneNumbers(5L))
                .thenThrow(new ResourceNotFoundException("No numbers found"));

            // Act & Assert: Call the method that invokes the static method
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerService.getCustomerPhoneNumbers(5L));  // You need to invoke the correct method.

            // Assert: Check if the exception message matches the expected message
            assertEquals("No numbers found", exception.getMessage());
        }
    }

}
