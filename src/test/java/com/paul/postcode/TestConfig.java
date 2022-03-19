package com.paul.postcode;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@TestConfiguration
public class TestConfig {
    @Bean("postcode")
    public HttpClient postcodeHttpClient() {
        return Mockito.mock(HttpClient.class);
    }
    @Bean("crimes")
    public HttpClient crimesHttpClient() {
        return Mockito.mock(HttpClient.class);
    }
}
