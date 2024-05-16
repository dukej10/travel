package com.dukez.best_travel.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info= @Info(title= "Best Travel API", version="1.0", description="Documentation for endpoints in Best Travel API")
)
public class OpenApiConfig {
}
