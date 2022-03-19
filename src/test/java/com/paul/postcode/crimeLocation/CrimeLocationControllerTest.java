package com.paul.postcode.crimeLocation;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.paul.postcode.TestData.CrimeJson;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

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
    public void shouldReturnCrime_PostcodeAndCrimesAPI_working() throws Exception {
        wireMockServer.stubFor(get("/postcodes/s71gq")
                .willReturn(ok()
                        .withStatus(200)
                        .withBody("{\"status\": 200,\"result\": {\"longitude\": 1.1,\"latitude\": 50.1}}")));

        wireMockServer.stubFor(get("/api/crimes-at-location?date=2017-02&lat=50.1&lng=1.1")
                .willReturn(ok()
                        .withStatus(200)
                        .withBody(CrimeJson)));

        String expected = "{\"crimes\":[{\"category\":\"violent-crime\",\"street\":\"On or near Abbey Gate\",\"outcome\":\"Unable to prosecute suspect\",\"month\":\"2017-02\"}]}";
        String actual = this.restTemplate.getForObject("http://localhost:" + port + "/crimes-at-postcode/s71gq", String.class);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldReturnCrime_CrimesAPIFailing() throws Exception {
        wireMockServer.stubFor(get("/postcodes/s71gq")
                .willReturn(ok()
                        .withStatus(200)
                        .withBody("{\"status\": 200,\"result\": {\"longitude\": 1.1,\"latitude\": 50.1}}")));

        wireMockServer.stubFor(get("/api/crimes-at-location?date=2017-02&lat=50.1&lng=1.1")
                .willReturn(ok()
                        .withStatus(500)
                        .withBody("")));

        String expected = "{\"errorMessage\":\"There was an issue with the Police Data API\"}";
        String actual = this.restTemplate.getForObject("http://localhost:" + port + "/crimes-at-postcode/s71gq", String.class);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void shouldReturnCrime_PostcodeAPINotFound() throws Exception {
        wireMockServer.stubFor(get("/postcodes/s73gq")
                .willReturn(ok()
                        .withStatus(404)
                        .withBody("{\"status\": 404,\"error\": \"Postcode not found\"}")));

        String expected = "{\"errorMessage\":\"Postcode not Found\"}";
        String actual = this.restTemplate.getForObject("http://localhost:" + port + "/crimes-at-postcode/s73gq", String.class);

        assertThat(actual, sameBeanAs(expected));
    }
}