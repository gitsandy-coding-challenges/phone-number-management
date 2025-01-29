package com.telecom.phone.number.management.integration;

import com.telecom.phone.number.management.controller.PhoneNumberController;
import com.telecom.phone.number.management.model.PhoneNumber;
import com.telecom.phone.number.management.model.PhoneNumbersResponse;
import com.telecom.phone.number.management.exception.ResourceNotFoundException;
import com.telecom.phone.number.management.service.PhoneNumberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link com.telecom.phone.number.management.controller.PhoneNumberController}.
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(PhoneNumberController.class)
public class PhoneNumberControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhoneNumberService phoneNumberService;

    @Test
    void testGetAllPhoneNumbers_NoPhoneNumbers() throws Exception {

        when(phoneNumberService.getAllPhoneNumbers()).thenThrow(new ResourceNotFoundException("No phone numbers found"));

        mockMvc.perform(get("/phone-numbers"))
            .andExpect(status().isNotFound())
            .andExpect(result -> {
                assert (result.getResolvedException() instanceof ResourceNotFoundException);
            });
    }

    @Test
    void testGetAllPhoneNumbers_Success() throws Exception {
        // Assuming we want to return a list of phone numbers
        PhoneNumbersResponse phoneNumbersResponse = new PhoneNumbersResponse();
        phoneNumbersResponse.setPhoneNumbers(Collections.singletonList(new PhoneNumber().number("1234567890").isActive(false)));
        when(phoneNumberService.getAllPhoneNumbers()).thenReturn(phoneNumbersResponse);

        // Perform a GET request to the controller's endpoint
        mockMvc.perform(get("/phone-numbers"))
            .andExpect(status().isOk())
            .andExpect(result -> {
                assert (result.getResponse().getContentAsString().contains("1234567890"));
            });
    }
}
