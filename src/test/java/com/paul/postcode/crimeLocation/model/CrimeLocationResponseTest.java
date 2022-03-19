package com.paul.postcode.crimeLocation.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;


class CrimeLocationResponseTest {

    @Test
    public void serialize_hasOnlyCrimes_showsOnlyCrimes() throws JsonProcessingException {
        Crime crime = Crime.builder().street("S").outcome("O").month("07-2017").category("C").build();
        CrimeLocationResponse crimeLocationResponse = new CrimeLocationResponse(new Crime[]{crime});
        String expected = "{\"crimes\":[{\"category\":\"C\",\"street\":\"S\",\"outcome\":\"O\",\"month\":\"07-2017\"}]}";
        String actual = new ObjectMapper().writeValueAsString(crimeLocationResponse);
        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    public void serialize_hasOnlyErrorMessage_showsOnlyErrorMessage() throws JsonProcessingException {
        CrimeLocationResponse crimeLocationResponse = new CrimeLocationResponse("Error Message");
        String expected = "{\"errorMessage\":\"Error Message\"}";
        String actual = new ObjectMapper().writeValueAsString(crimeLocationResponse);
        assertThat(actual, sameBeanAs(expected));
    }
}