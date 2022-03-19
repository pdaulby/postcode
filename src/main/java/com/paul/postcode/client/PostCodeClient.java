package com.paul.postcode.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.postcode.client.model.PostCodeResult;
import com.paul.postcode.client.model.PostCodeResultBody;
import com.paul.postcode.tinytype.HttpError;
import io.vavr.control.Either;
import org.apache.commons.lang3.StringUtils;
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
public class PostCodeClient {

    @Value("${postcode-api}")
    private String host;
    private String api =  "/postcodes/";
    @Autowired
    @Qualifier("postcode")
    private HttpClient client;

    public Either<HttpError, PostCodeResult> getPostcodeResult(String postcode) {
        System.out.println(host + api);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(host + api + StringUtils.deleteWhitespace(postcode)))
                .header("Accept", "application/json")
                .GET()
                .build();
        try {
            return getResult(request);
        } catch (IOException | InterruptedException e) {
            return Either.left(new HttpError(HttpStatus.INTERNAL_SERVER_ERROR, "There was an issue reading the postcode data"));
        }
    }

    private Either<HttpError, PostCodeResult> getResult(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 404) {
            return Either.left(new HttpError(HttpStatus.NOT_FOUND, "Postcode not Found"));
        }
        if (httpResponse.statusCode() != 200) {
            return Either.left(new HttpError(HttpStatus.BAD_GATEWAY, "There was an issue with the Postcode API"));
        }
        return Either.right(new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(httpResponse.body(), PostCodeResultBody.class)
                .getResult());
    }
}