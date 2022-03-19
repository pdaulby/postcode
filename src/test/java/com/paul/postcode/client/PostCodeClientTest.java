package com.paul.postcode.client;

import com.paul.postcode.TestConfig;
import com.paul.postcode.client.model.PostCodeResult;
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

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = TestConfig.class)
@SpringBootTest(properties= "spring.main.allow-bean-definition-overriding=true")
class PostCodeClientTest {
    @Autowired
    private PostCodeClient postCodeClient;
    @Autowired()
    @Qualifier("postcode")
    private HttpClient mockHttpClient;

    @Test
    public void PostCodeClientTest() throws IOException, InterruptedException {
        String responseBody = "{\"status\": 200,\"result\": {\"longitude\": -1.484746,\"latitude\": 53.359747}}";
        MockRequest("S71GQ", 200, responseBody);

        Either<HttpError, PostCodeResult> expected = Either.right(new PostCodeResult(53.359747f, -1.484746f));
        Either<HttpError, PostCodeResult> actual = postCodeClient.getPostcodeResult("S71GQ");

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void PostCodeClientTest_spaces() throws IOException, InterruptedException {
        String responseBody = "{\"status\": 200,\"result\": {\"longitude\": -1.484746,\"latitude\": 53.359747}}";
        MockRequest("S71GQ", 200, responseBody);

        Either<HttpError, PostCodeResult> expected = Either.right(new PostCodeResult(53.359747f, -1.484746f));
        Either<HttpError, PostCodeResult> actual = postCodeClient.getPostcodeResult("S7 1GQ");

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void PostCodeClientTest_malformedResponse() throws IOException, InterruptedException {
        String responseBody = "{\"statgitu84746,\"latitude\": 53.359747}}";
        MockRequest("S71GQ", 200, responseBody);

        Either<HttpError, PostCodeResult> expected = Either.left(new HttpError(HttpStatus.INTERNAL_SERVER_ERROR, "There was an issue reading the postcode data"));
        Either<HttpError, PostCodeResult> actual = postCodeClient.getPostcodeResult("S7 1GQ");

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void PostCodeClientTest_404() throws IOException, InterruptedException {
        String responseBody = "{\"status\": 200,\"result\": {\"longitude\": -1.484746,\"latitude\": 53.359747}}";
        MockRequest("S73GQ", 404, responseBody);

        Either<HttpError, PostCodeResult> expected = Either.left(new HttpError(HttpStatus.NOT_FOUND, "Postcode not Found"));
        Either<HttpError, PostCodeResult> actual =  postCodeClient.getPostcodeResult("S73GQ");

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void PostCodeClientTest_500() throws IOException, InterruptedException {
        String responseBody = "{\"status\": 200,\"result\": {\"longitude\": -1.484746,\"latitude\": 53.359747}}";
        MockRequest("S73GQ", 500, responseBody);

        Either<HttpError, PostCodeResult> expected = Either.left(new HttpError(HttpStatus.BAD_GATEWAY, "There was an issue with the Postcode API"));
        Either<HttpError, PostCodeResult> actual =  postCodeClient.getPostcodeResult("S73GQ");

        assertThat(actual, sameBeanAs(expected));
    }

    private void MockRequest(String postcode, int statusCode, String responseBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.postcodes.io/postcodes/" + postcode))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(statusCode);
        when(response.body()).thenReturn(responseBody);

        when(mockHttpClient.send(request, HttpResponse.BodyHandlers.ofString())).thenReturn(response);
    }
}