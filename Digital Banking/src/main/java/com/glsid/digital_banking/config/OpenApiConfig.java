package com.glsid.digital_banking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                // Add server information to handle context path
                .servers(List.of(
                    new io.swagger.v3.oas.models.servers.Server()
                        .url("/api")
                        .description("API with context path")
                ))
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT",
                                new SecurityScheme()
                                        .name("JWT")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("JWT token authentication")));
    }
}
