package com.ecommerce.project.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT Bearer Token");
        SecurityRequirement bearerRequirement = new SecurityRequirement()
                .addList("bearer Authentication");
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot e- commerce application")
                        .version("1.0")
                        .description("Spring Boot e- commerce application")
                        .license(new License().name("Apache 2.o0").url("http://ecom.com")
                                ).contact(new Contact()
                                .name("Anant Gupta")
                                .url("https://github.com/anantg840-coder"))
                        ).externalDocs(new ExternalDocumentation()
                        .description("Spring Boot e- commerce application")
                        .url("https://github.com/anantg840-coder"))

                .components(new Components()
                        .addSecuritySchemes("bearer Authentication", bearerScheme ))
                .addSecurityItem(bearerRequirement);



    }

}
