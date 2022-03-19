package com.paul.postcode.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.postcode.client.model.CrimeResult;
import com.paul.postcode.client.model.PostCodeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Component
public class CrimesClient {
    private static final String baseUrl = "https://data.police.uk/api/crimes-at-location";
    @Autowired
    @Qualifier("crimes")
    private HttpClient client;

    public Optional<CrimeResult[]> getCrimes(PostCodeResult postCodeResult) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "?date=2017-02&lat=" + postCodeResult.getLatitude() + "&lng=" + postCodeResult.getLongitude()))
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            return getCrimeResults(request);
        } catch (IOException | InterruptedException e) {
            return Optional.empty();
        }
    }

    private Optional<CrimeResult[]> getCrimeResults(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() != 200) {
            return Optional.empty();
        }
        String body = httpResponse.body();
        return Optional.of(new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(body, CrimeResult[].class));
    }
}
