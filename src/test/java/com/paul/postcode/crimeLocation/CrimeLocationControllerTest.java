package com.paul.postcode.crimeLocation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties= "spring.main.allow-bean-definition-overriding=true", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrimeLocationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/crimes-at-postcode/s71gq", String.class))
                .contains("TODO mock api's");
    }
}