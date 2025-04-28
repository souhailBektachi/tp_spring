package com.glsid.digital_banking;

import com.glsid.digital_banking.dtos.CustomerDTO;
import com.glsid.digital_banking.entities.*;
import com.glsid.digital_banking.exceptions.CustomerNotFoundException;
import com.glsid.digital_banking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name.toLowerCase() + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 90000, 5.5, customer.getId());

                    // Add some operations
                    String accountId = bankAccountService.bankAccountList().get(0).getId();
                    
                    for (int i = 0; i < 5; i++) {
                        bankAccountService.credit(accountId, 1000 + Math.random() * 9000, "Credit operation");
                    }
                    
                    for (int i = 0; i < 3; i++) {
                        bankAccountService.debit(accountId, 1000 + Math.random() * 3000, "Debit operation");
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        };
    }
}
