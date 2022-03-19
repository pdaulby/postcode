package com.paul.postcode.crimeLocation;

import com.paul.postcode.client.CrimesClient;
import com.paul.postcode.client.PostCodeClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@TestConfiguration
public class MockedClientsConfig {
    public PostCodeClient crimesHttpClient() {
        return null;
    }
    public CrimesClient postcodeHttpClient() {
        return Mockito.mock(CrimesClient.class);
    }
}
