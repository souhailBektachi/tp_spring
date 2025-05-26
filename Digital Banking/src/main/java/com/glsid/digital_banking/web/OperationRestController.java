package com.glsid.digital_banking.web;

import com.glsid.digital_banking.dtos.AccountOperationDTO;
import com.glsid.digital_banking.exceptions.BalanceNotSufficientException;
import com.glsid.digital_banking.exceptions.BankAccountNotFoundException;
import com.glsid.digital_banking.services.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/operations")
@Tag(name = "Banking Operations")
public class OperationRestController {
    private BankAccountService bankAccountService;

    @PostMapping("/debit")
    @Operation(summary = "Debit operation", description = "Withdraw money from an account")
    public void debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
    }

    @PostMapping("/credit")
    @Operation(summary = "Credit operation", description = "Deposit money to an account")
    public void credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer operation", description = "Transfer money between accounts")
    public void transfer(@RequestBody TransferDTO transferDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.transfer(
                transferDTO.getAccountSource(),
                transferDTO.getAccountDestination(),
                transferDTO.getAmount());
    }

    @Data
    public static class DebitDTO {
        private String accountId;
        private double amount;
        private String description;
    }

    @Data
    public static class CreditDTO {
        private String accountId;
        private double amount;
        private String description;
    }

    @Data
    public static class TransferDTO {
        private String accountSource;
        private String accountDestination;
        private double amount;
    }
}
