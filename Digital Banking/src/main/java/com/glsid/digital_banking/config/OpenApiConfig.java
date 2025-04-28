package com.glsid.digital_banking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI digitalBankingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Banking API")
                        .description("API for Digital Banking application")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Digital Banking Team")
                                .email("contact@digitalbanking.com")
                                .url("https://digitalbanking.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
