package com.paul.postcode.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.postcode.client.model.PostCodeResult;
import com.paul.postcode.client.model.PostCodeResultBody;
import org.apache.commons.lang3.StringUtils;
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
public class PostCodeClient {
    private static final String baseUrl = "http://api.postcodes.io/postcodes/";
    @Autowired
    @Qualifier("postcode")
    private HttpClient client;

    public PostCodeClient() {
        System.out.println("TESTESTEST");
    }

    public Optional<PostCodeResult> getPostcodeResult(String postcode) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + StringUtils.deleteWhitespace(postcode)))
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            return getResult(request);
        } catch (IOException | InterruptedException e) {
            return Optional.empty();
        }
    }

    private Optional<PostCodeResult> getResult(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() != 200) {
            return Optional.empty();
        }
        return Optional.of(new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(httpResponse.body(), PostCodeResultBody.class)
                .getResult());
    }
}