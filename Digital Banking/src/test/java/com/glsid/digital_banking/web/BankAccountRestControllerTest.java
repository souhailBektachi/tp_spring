package com.glsid.digital_banking.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glsid.digital_banking.dtos.*;
import com.glsid.digital_banking.entities.AccountStatus;
import com.glsid.digital_banking.exceptions.BankAccountNotFoundException;
import com.glsid.digital_banking.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankAccountRestController.class)
public class BankAccountRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<BankAccountDTO> accounts;
    private CustomerDTO customerDTO;
    private CurrentAccountDTO currentAccountDTO;
    private SavingAccountDTO savingAccountDTO;

    @BeforeEach
    public void setup() {
        customerDTO = new CustomerDTO(1L, "John Doe", "john@example.com");
        
        currentAccountDTO = new CurrentAccountDTO();
        currentAccountDTO.setId("ca-123");
        currentAccountDTO.setBalance(5000);
        currentAccountDTO.setCreatedAt(new Date());
        currentAccountDTO.setStatus(AccountStatus.CREATED);
        currentAccountDTO.setCustomerDTO(customerDTO);
        currentAccountDTO.setOverDraft(2000);
        currentAccountDTO.setType("CurrentAccount");
        
        savingAccountDTO = new SavingAccountDTO();
        savingAccountDTO.setId("sa-456");
        savingAccountDTO.setBalance(10000);
        savingAccountDTO.setCreatedAt(new Date());
        savingAccountDTO.setStatus(AccountStatus.CREATED);
        savingAccountDTO.setCustomerDTO(customerDTO);
        savingAccountDTO.setInterestRate(3.5);
        savingAccountDTO.setType("SavingAccount");
        
        this.accounts = Arrays.asList(currentAccountDTO, savingAccountDTO);
    }

    @Test
    public void shouldReturnAllAccounts() throws Exception {
        given(bankAccountService.bankAccountList()).willReturn(accounts);

        mockMvc.perform(get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("ca-123")))
                .andExpect(jsonPath("$[1].id", is("sa-456")))
                .andExpect(jsonPath("$[0].type", is("CurrentAccount")))
                .andExpect(jsonPath("$[1].type", is("SavingAccount")));
    }

    @Test
    public void shouldReturnAccountById() throws Exception {
        String accountId = "ca-123";
        
        given(bankAccountService.getBankAccount(accountId)).willReturn(currentAccountDTO);

        mockMvc.perform(get("/api/accounts/{id}", accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(accountId)))
                .andExpect(jsonPath("$.type", is("CurrentAccount")))
                .andExpect(jsonPath("$.balance", is(5000.0)));
    }

    @Test
    public void shouldReturn404WhenAccountNotFound() throws Exception {
        String accountId = "non-existent";
        
        given(bankAccountService.getBankAccount(accountId))
            .willThrow(new BankAccountNotFoundException("Bank account not found"));

        mockMvc.perform(get("/api/accounts/{id}", accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnAccountOperations() throws Exception {
        String accountId = "ca-123";
        List<AccountOperationDTO> operations = Arrays.asList(
            new AccountOperationDTO(1L, new Date(), 1000, null, "Deposit"),
            new AccountOperationDTO(2L, new Date(), 500, null, "Withdrawal")
        );
        
        given(bankAccountService.accountHistory(accountId)).willReturn(operations);

        mockMvc.perform(get("/api/accounts/{id}/operations", accountId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].amount", is(1000.0)))
                .andExpect(jsonPath("$[1].amount", is(500.0)));
    }

    @Test
    public void shouldCreateCurrentAccount() throws Exception {
        given(bankAccountService.saveCurrentBankAccount(anyDouble(), anyDouble(), anyLong()))
            .willReturn(currentAccountDTO);

        mockMvc.perform(post("/api/accounts/current")
                .param("initialBalance", "5000")
                .param("overDraft", "2000")
                .param("customerId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("ca-123")))
                .andExpect(jsonPath("$.type", is("CurrentAccount")))
                .andExpect(jsonPath("$.overDraft", is(2000.0)));
    }

    @Test
    public void shouldCreateSavingAccount() throws Exception {
        given(bankAccountService.saveSavingBankAccount(anyDouble(), anyDouble(), anyLong()))
            .willReturn(savingAccountDTO);

        mockMvc.perform(post("/api/accounts/saving")
                .param("initialBalance", "10000")
                .param("interestRate", "3.5")
                .param("customerId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("sa-456")))
                .andExpect(jsonPath("$.type", is("SavingAccount")))
                .andExpect(jsonPath("$.interestRate", is(3.5)));
    }
}
