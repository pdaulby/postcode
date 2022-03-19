package com.paul.postcode.crimeLocation;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.paul.postcode.TestData.CrimeJson;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.paul.postcode.crimeLocation.model.Crime;
import com.paul.postcode.crimeLocation.model.CrimeLocationResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(properties = {"spring.main.allow-bean-definition-overriding=true", "postcode-api=http://localhost:8089", "crimes-api=http://localhost:8089"},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrimeLocationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    WireMockServer wireMockServer;
    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
    }
    @AfterEach
    void cleanup() {
        wireMockServer.stop();
    }

    @Test
    public void shouldReturnCrime_PostcodeAndCrimesAPI_working() {
        wireMockServer.stubFor(get("/postcodes/s71gq")
                .willReturn(ok()
                        .withStatus(200)
                        .withBody("{\"status\": 200,\"result\": {\"longitude\": 1.1,\"latitude\": 50.1}}")));

        wireMockServer.stubFor(get("/api/crimes-at-location?date=2017-02&lat=50.1&lng=1.1")
                .willReturn(ok()
                        .withStatus(200)
                        .withBody(CrimeJson)));

        Crime expectedCrime = Crime.builder()
                .month("2017-02")
                .outcome("Unable to prosecute suspect")
                .street("On or near Abbey Gate")
                .category("violent-crime")
                .build();

        ResponseEntity<CrimeLocationResponse> expected =
                ResponseEntity.status(200).body(new CrimeLocationResponse(new Crime[]{ expectedCrime }));
        ResponseEntity<CrimeLocationResponse> actual =
                this.restTemplate.getForEntity("http://localhost:" + port + "/crimes-at-postcode/s71gq", CrimeLocationResponse.class);

        assertThat(actual, sameBeanAs(expected).ignoring("headers"));
    }

    @Test
    public void shouldReturnCrime_CrimesAPIFailing() {
        wireMockServer.stubFor(get("/postcodes/s71gq")
                .willReturn(ok()
                        .withStatus(200)
                        .withBody("{\"status\": 200,\"result\": {\"longitude\": 1.1,\"latitude\": 50.1}}")));

        wireMockServer.stubFor(get("/api/crimes-at-location?date=2017-02&lat=50.1&lng=1.1")
                .willReturn(ok()
                        .withStatus(500)
                        .withBody("")));

        ResponseEntity<CrimeLocationResponse> expected =
                ResponseEntity.status(502).body(new CrimeLocationResponse("There was an issue with the Police Data API"));
        ResponseEntity<CrimeLocationResponse> actual =
                this.restTemplate.getForEntity("http://localhost:" + port + "/crimes-at-postcode/s71gq", CrimeLocationResponse.class);

        assertThat(actual, sameBeanAs(expected).ignoring("headers"));
    }

    @Test
    public void shouldReturnCrime_PostcodeAPINotFound() {
        wireMockServer.stubFor(get("/postcodes/s73gq")
                .willReturn(ok()
                        .withStatus(404)
                        .withBody("{\"status\": 404,\"error\": \"Postcode not found\"}")));

        ResponseEntity<CrimeLocationResponse> expected =
                ResponseEntity.status(404).body(new CrimeLocationResponse("Postcode not Found"));
        ResponseEntity<CrimeLocationResponse> actual =
                this.restTemplate.getForEntity("http://localhost:" + port + "/crimes-at-postcode/s73gq", CrimeLocationResponse.class);

        assertThat(actual, sameBeanAs(expected).ignoring("headers"));
    }



}