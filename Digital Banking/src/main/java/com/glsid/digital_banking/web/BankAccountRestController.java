package com.glsid.digital_banking.web;

import com.glsid.digital_banking.dtos.*;
import com.glsid.digital_banking.exceptions.BankAccountNotFoundException;
import com.glsid.digital_banking.exceptions.CustomerNotFoundException;
import com.glsid.digital_banking.services.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/accounts")
@Tag(name = "Bank Account Management")
@CrossOrigin("*")
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Returns a list of all bank accounts")
    public List<BankAccountDTO> bankAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Returns bank account details by ID")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(id);
    }

    @GetMapping("/{id}/operations")
    @Operation(summary = "Get account operations", description = "Returns a list of operations for a specific account")
    public List<AccountOperationDTO> getAccountOperations(@PathVariable String id) {
        return bankAccountService.accountHistory(id);
    }

    @PostMapping("/current")
    @Operation(summary = "Create current account", description = "Creates a new current account for a customer")
    public CurrentAccountDTO createCurrentAccount(@RequestParam double initialBalance,
                                                 @RequestParam double overDraft,
                                                 @RequestParam Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
    }

    @PostMapping("/saving")
    @Operation(summary = "Create saving account", description = "Creates a new saving account for a customer")
    public SavingAccountDTO createSavingAccount(@RequestParam double initialBalance,
                                              @RequestParam double interestRate,
                                              @RequestParam Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
    }
}
