package com.glsid.digital_banking.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glsid.digital_banking.exceptions.BalanceNotSufficientException;
import com.glsid.digital_banking.exceptions.BankAccountNotFoundException;
import com.glsid.digital_banking.services.BankAccountService;
import com.glsid.digital_banking.web.OperationRestController.CreditDTO;
import com.glsid.digital_banking.web.OperationRestController.DebitDTO;
import com.glsid.digital_banking.web.OperationRestController.TransferDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OperationRestController.class)
public class OperationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldDebitAccount() throws Exception {
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setAccountId("ca-123");
        debitDTO.setAmount(500);
        debitDTO.setDescription("Test Withdrawal");
        
        doNothing().when(bankAccountService).debit(anyString(), anyDouble(), anyString());

        mockMvc.perform(post("/api/operations/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(debitDTO)))
                .andExpect(status().isOk());
                
        Mockito.verify(bankAccountService).debit("ca-123", 500, "Test Withdrawal");
    }

    @Test
    public void shouldReturnNotFoundForDebitWhenAccountNotFound() throws Exception {
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setAccountId("non-existent");
        debitDTO.setAmount(500);
        debitDTO.setDescription("Test Withdrawal");
        
        doThrow(new BankAccountNotFoundException("Bank account not found"))
            .when(bankAccountService).debit(anyString(), anyDouble(), anyString());

        mockMvc.perform(post("/api/operations/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(debitDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestForDebitWhenBalanceNotSufficient() throws Exception {
        DebitDTO debitDTO = new DebitDTO();
        debitDTO.setAccountId("ca-123");
        debitDTO.setAmount(50000);
        debitDTO.setDescription("Test Withdrawal");
        
        doThrow(new BalanceNotSufficientException("Balance not sufficient"))
            .when(bankAccountService).debit(anyString(), anyDouble(), anyString());

        mockMvc.perform(post("/api/operations/debit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(debitDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCreditAccount() throws Exception {
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setAccountId("ca-123");
        creditDTO.setAmount(1000);
        creditDTO.setDescription("Test Deposit");
        
        doNothing().when(bankAccountService).credit(anyString(), anyDouble(), anyString());

        mockMvc.perform(post("/api/operations/credit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creditDTO)))
                .andExpect(status().isOk());
                
        Mockito.verify(bankAccountService).credit("ca-123", 1000, "Test Deposit");
    }

    @Test
    public void shouldTransferBetweenAccounts() throws Exception {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setAccountSource("ca-123");
        transferDTO.setAccountDestination("sa-456");
        transferDTO.setAmount(2000);
        
        doNothing().when(bankAccountService).transfer(anyString(), anyString(), anyDouble());

        mockMvc.perform(post("/api/operations/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferDTO)))
                .andExpect(status().isOk());
                
        Mockito.verify(bankAccountService).transfer("ca-123", "sa-456", 2000);
    }
}
