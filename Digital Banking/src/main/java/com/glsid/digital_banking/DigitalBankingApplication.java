package com.glsid.digital_banking;

import com.glsid.digital_banking.dtos.CustomerDTO;
import com.glsid.digital_banking.entities.*;
import com.glsid.digital_banking.exceptions.CustomerNotFoundException;
import com.glsid.digital_banking.repositories.RoleRepository;
import com.glsid.digital_banking.repositories.UserRepository;
import com.glsid.digital_banking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner initSecurityData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            if (roleRepository.findByName("ADMIN") == null) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                roleRepository.save(adminRole);
            }
            
            if (roleRepository.findByName("USER") == null) {
                Role userRole = new Role();
                userRole.setName("USER");
                roleRepository.save(userRole);
            }
            
            // Create admin user if it doesn't exist
            if (userRepository.findByUsername("admin") == null) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin"));
                adminUser.setActive(true);
                
                ArrayList<Role> adminRoles = new ArrayList<>();
                adminRoles.add(roleRepository.findByName("ADMIN"));
                adminUser.setRoles(adminRoles);
                
                userRepository.save(adminUser);
            }
        };
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
