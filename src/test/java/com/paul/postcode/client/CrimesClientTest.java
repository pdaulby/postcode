package com.paul.postcode.client;

import com.paul.postcode.TestConfig;
import com.paul.postcode.client.model.*;
import com.paul.postcode.tinytype.HttpError;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.paul.postcode.TestData.CrimeJson;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = TestConfig.class)
@SpringBootTest(properties= "spring.main.allow-bean-definition-overriding=true")
class CrimesClientTest {
    @Autowired
    private CrimesClient crimesClient;
    @Autowired()
    @Qualifier("crimes")
    private HttpClient mockHttpClient;

    @Test
    public void CrimesClientTest() throws IOException, InterruptedException {
        MockRequest(52.643950f,-1.143042f, 200, CrimeJson);

        Either<HttpError, CrimeResult[]> expected = Either.right(new CrimeResult[] {
                CrimeResult.builder().category("violent-crime")
                        .location_type("Force")
                        .location(CrimeLocation.builder()
                                .latitude("52.643950")
                                .longitude("-1.143042")
                                .street(new CrimeStreet(884227, "On or near Abbey Gate"))
                                .build())
                        .context("")
                        .outcome_status(new CrimeOutcome("Unable to prosecute suspect", "2017-02"))
                        .persistent_id("4d83433f3117b3a4d2c80510c69ea188a145bd7e94f3e98924109e70333ff735")
                        .id(54726925)
                        .location_subtype("")
                        .month("2017-02")
                        .build()
        });

        Either<HttpError, CrimeResult[]> actual = crimesClient.getCrimes(new PostCodeResult(52.643950f, -1.143042f));

        assertThat(actual, sameBeanAs(expected));
    }
    @Test
    public void CrimesClientTest_500() throws IOException, InterruptedException {
        MockRequest(52.643950f,-1.143042f, 500, "");

        Either<HttpError, CrimeResult[]> expected = Either.left(new HttpError(HttpStatus.BAD_GATEWAY, "There was an issue with the Police Data API"));
        Either<HttpError, CrimeResult[]> actual = crimesClient.getCrimes(new PostCodeResult(52.643950f, -1.143042f));

        assertThat(actual, sameBeanAs(expected));
    }
    @Test
    public void CrimesClientTest_malformedResponse() throws IOException, InterruptedException {
        MockRequest(52.643950f,-1.143042f, 200, "[{");

        Either<HttpError, CrimeResult[]> expected = Either.left(new HttpError(HttpStatus.INTERNAL_SERVER_ERROR, "There was an issue reading the crime data"));
        Either<HttpError, CrimeResult[]> actual = crimesClient.getCrimes(new PostCodeResult(52.643950f, -1.143042f));

        assertThat(actual, sameBeanAs(expected));
    }

    private void MockRequest(float latitude, float longitude, int statusCode, String responseBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://data.police.uk/api/crimes-at-location?date=2017-02&lat=" + latitude + "&lng=" + longitude))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(statusCode);
        when(response.body()).thenReturn(responseBody);

        when(mockHttpClient.send(request, HttpResponse.BodyHandlers.ofString())).thenReturn(response);
    }

}