package com.glsid.digital_banking.web;

import com.glsid.digital_banking.exceptions.BalanceNotSufficientException;
import com.glsid.digital_banking.exceptions.BankAccountNotFoundException;
import com.glsid.digital_banking.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleCustomerNotFoundException(CustomerNotFoundException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", exception.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(BankAccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleBankAccountNotFoundException(BankAccountNotFoundException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", exception.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(BalanceNotSufficientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBalanceNotSufficientException(BalanceNotSufficientException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", exception.getMessage());
        return errorResponse;
    }
}
