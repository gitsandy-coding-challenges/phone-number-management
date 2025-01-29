package com.telecom.phone.number.management.integration;


import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.model.PhoneNumber;
import com.telecom.phone.number.management.model.PhoneNumbersResponse;
import com.telecom.phone.number.management.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration tests for {@link com.telecom.phone.number.management.controller.CustomerController}.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    CustomerService customerService;

    @Test
    void testGetCustomerPhoneNumbers_Success() throws Exception {
        PhoneNumbersResponse phoneNumbersResponse = new PhoneNumbersResponse();
        phoneNumbersResponse.setPhoneNumbers(Collections.singletonList(new PhoneNumber().number("1234567890").isActive(false)));
        when(customerService.getCustomerPhoneNumbers(1L)).thenReturn(Optional.of(phoneNumbersResponse));
        mockMvc.perform(get("/customers/{customerId}/phone-numbers", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.phoneNumbers[0].number").value("1234567890"));
            //.andExpect(jsonPath("$.phoneNumbers[1].number").value("9876543210"));
    }

    @Test
    void testGetCustomerPhoneNumbers_CustomerNotFound() throws Exception {
        mockMvc.perform(get("/customers/{customerId}/phone-numbers", 999L))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }

    @Test
    void testActivatePhoneNumber_Success() throws Exception {

        PhoneNumbersResponse phoneNumbersResponse = new PhoneNumbersResponse();
        phoneNumbersResponse.setPhoneNumbers(Collections.singletonList(new PhoneNumber().number("1234567890").isActive(true)));
        when(customerService.activatePhoneNumber(1L, "1234567890")).thenReturn(Optional.of(phoneNumbersResponse));

        mockMvc.perform(patch("/customers/{customerId}/phone-numbers/{phoneNumber}", 1L, "1234567890"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.phoneNumbers[0].number").value("1234567890"))
            .andExpect(jsonPath("$.phoneNumbers[0].isActive").value("true"));
    }

    @Test
    void testActivatePhoneNumber_Failure() throws Exception {
        mockMvc.perform(patch("/customers/{customerId}/phone-numbers/{phoneNumber}", 1L, "9876543213"))
            .andExpect(status().isNotFound())
            .andExpect(result -> assertInstanceOf(ResourceNotFoundException.class, result.getResolvedException()));
    }
}
