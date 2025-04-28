package com.glsid.digital_banking.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glsid.digital_banking.dtos.CustomerDTO;
import com.glsid.digital_banking.exceptions.CustomerNotFoundException;
import com.glsid.digital_banking.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerRestController.class)
public class CustomerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<CustomerDTO> customers;

    @BeforeEach
    public void setup() {
        this.customers = Arrays.asList(
            new CustomerDTO(1L, "John Doe", "john@example.com"),
            new CustomerDTO(2L, "Jane Doe", "jane@example.com")
        );
    }

    @Test
    public void shouldReturnAllCustomers() throws Exception {
        given(bankAccountService.listCustomers()).willReturn(customers);

        mockMvc.perform(get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Doe")));
    }

    @Test
    public void shouldReturnCustomerById() throws Exception {
        Long customerId = 1L;
        CustomerDTO customer = new CustomerDTO(customerId, "John Doe", "john@example.com");
        
        given(bankAccountService.getCustomer(customerId)).willReturn(customer);

        mockMvc.perform(get("/api/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    public void shouldReturn404WhenCustomerNotFound() throws Exception {
        Long customerId = 999L;
        
        given(bankAccountService.getCustomer(customerId))
            .willThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateCustomer() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO(null, "New Customer", "new@example.com");
        CustomerDTO savedCustomer = new CustomerDTO(3L, "New Customer", "new@example.com");
        
        given(bankAccountService.saveCustomer(any(CustomerDTO.class))).willReturn(savedCustomer);

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("New Customer")));
    }

    @Test
    public void shouldUpdateCustomer() throws Exception {
        Long customerId = 1L;
        CustomerDTO customerDTO = new CustomerDTO(null, "Updated Customer", "updated@example.com");
        CustomerDTO updatedCustomer = new CustomerDTO(customerId, "Updated Customer", "updated@example.com");
        
        given(bankAccountService.updateCustomer(any(CustomerDTO.class))).willReturn(updatedCustomer);

        mockMvc.perform(put("/api/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Customer")));
    }

    @Test
    public void shouldDeleteCustomer() throws Exception {
        Long customerId = 1L;
        
        doNothing().when(bankAccountService).deleteCustomer(customerId);

        mockMvc.perform(delete("/api/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                
        verify(bankAccountService).deleteCustomer(customerId);
    }
}
