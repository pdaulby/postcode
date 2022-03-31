package com.paul.postcode.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.postcode.client.model.CrimeResult;
import com.paul.postcode.client.model.PostCodeResult;
import com.paul.postcode.tinytype.HttpError;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class CrimesClient {
    @Value("${crimes-api}")
    private String host;
    private String api = "/api/crimes-at-location";
    @Autowired
    @Qualifier("crimes")
    private HttpClient client;

    public Either<HttpError, CrimeResult[]> getCrimes(PostCodeResult postCodeResult) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(host + api + "?date=2021-02&lat=" + postCodeResult.getLatitude() + "&lng=" + postCodeResult.getLongitude()))
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            return getCrimeResults(request);
        } catch (IOException | InterruptedException e) {
            return Either.left(new HttpError(HttpStatus.INTERNAL_SERVER_ERROR, "There was an issue reading the crime data"));
        }
    }

    private Either<HttpError, CrimeResult[]> getCrimeResults(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (httpResponse.statusCode() != 200) {
            return Either.left(new HttpError(HttpStatus.BAD_GATEWAY, "There was an issue with the Police Data API"));
        }
        String body = httpResponse.body();
        var x = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(body, CrimeResult[].class);
        return Either.right(x);
    }
}
