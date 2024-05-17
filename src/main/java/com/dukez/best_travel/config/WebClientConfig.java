package com.dukez.best_travel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value(value = "${apiLayer.base.url}")
    private String baseUrl;

    @Value(value = "${apiLayer.api-key}")
    private String apiKey;

    @Value(value = "${apiLayer.api-key.header}")
    private String apiKeyHeader;

    @Bean(name = "currency")
    public WebClient currencyWebClient() {
        System.out.println("baseUrl: " + baseUrl);
        System.out.println("apiKey: " + apiKey);
        System.out.println("apiKeyHeader: " + apiKeyHeader);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(apiKeyHeader, apiKey)
                .build();
    }

}
