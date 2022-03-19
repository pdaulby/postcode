package com.paul.postcode.crimeLocation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.postcode.client.model.CrimeResult;
import com.paul.postcode.crimeLocation.model.Crime;
import com.paul.postcode.crimeLocation.model.CrimeLocationResponse;
import com.paul.postcode.crimeLocation.model.CrimeLocationResponseMapper;
import org.junit.jupiter.api.Test;

import static com.paul.postcode.TestData.CrimeJson;
import static com.paul.postcode.TestData.TwoCrimesJson;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;

class CrimeLocationResponseMapperTest {

    @Test
    public void testMapping() throws JsonProcessingException {
        CrimeResult[] crimeResults = new ObjectMapper().readValue(CrimeJson, CrimeResult[].class);

        CrimeLocationResponse expected = new CrimeLocationResponse(new Crime[] {
                Crime.builder()
                        .month("2017-02")
                        .outcome("Unable to prosecute suspect")
                        .street("On or near Abbey Gate")
                        .category("violent-crime")
                        .build()
        });
        CrimeLocationResponse actual = new CrimeLocationResponseMapper().mapLocationResponse(crimeResults);

        assertThat(actual, sameBeanAs(expected));
    }
    @Test
    public void testMapping_2Crimes() throws JsonProcessingException {
        CrimeResult[] crimeResults = new ObjectMapper().readValue(TwoCrimesJson, CrimeResult[].class);

        CrimeLocationResponse expected = new CrimeLocationResponse(new Crime[] {
                Crime.builder()
                        .month("2017-02")
                        .outcome("Unable to prosecute suspect")
                        .street("On or near Abbey Gate")
                        .category("violent-crime")
                        .build(),
                Crime.builder()
                        .month("2017-02")
                        .outcome("Unable to prosecute suspect")
                        .street("On or near Abbey Gate")
                        .category("violent-crime2")
                        .build()
        });
        CrimeLocationResponse actual = new CrimeLocationResponseMapper().mapLocationResponse(crimeResults);

        assertThat(actual, sameBeanAs(expected));
    }
    
    @Test
    public void testMapping_noCrimes() {
        CrimeLocationResponse expected = new CrimeLocationResponse(new Crime[] {});
        CrimeLocationResponse actual = new CrimeLocationResponseMapper().mapLocationResponse(new CrimeResult[]{});

        assertThat(actual, sameBeanAs(expected));
    }

}