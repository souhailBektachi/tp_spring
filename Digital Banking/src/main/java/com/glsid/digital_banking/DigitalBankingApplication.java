package com.glsid.digital_banking;

import com.glsid.digital_banking.service.DataInitializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class DigitalBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner initSecurityData(DataInitializationService dataInitializationService) {
        return args -> {
            dataInitializationService.initializeSecurityData();
        };
    }
}