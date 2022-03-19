package com.paul.postcode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class AppConfig {
    @Bean("postcode")
    public HttpClient postcodeHttpClient() {
        return HttpClient.newHttpClient();
    }
    @Bean("crimes")
    public HttpClient crimesHttpClient() {
        return HttpClient.newHttpClient();
    }
}
