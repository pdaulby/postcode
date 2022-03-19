package com.paul.postcode.crimeLocation;

import com.paul.postcode.TestConfig;
import com.paul.postcode.client.CrimesClient;
import com.paul.postcode.client.PostCodeClient;
import com.paul.postcode.client.model.*;
import com.paul.postcode.crimeLocation.model.Crime;
import com.paul.postcode.crimeLocation.model.CrimeLocationResponse;
import com.paul.postcode.tinytype.HttpError;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = TestConfig.class)
@SpringBootTest(properties= "spring.main.allow-bean-definition-overriding=true")
@ExtendWith(MockitoExtension.class)
class CrimeLocationServiceTest {
    @Mock
    PostCodeClient mockPostcodeClient;
    @Mock
    CrimesClient mockCrimesClient;
    @Autowired
    @InjectMocks
    CrimeLocationService crimeLocationService;

    @Test
    void getCrimesAtPostcode_happyPath() {
        when(mockPostcodeClient.getPostcodeResult(postcode))
                .thenReturn(Either.right(postCodeResult));
        when(mockCrimesClient.getCrimes(postCodeResult))
                .thenReturn(Either.right(new CrimeResult[]{ crimeResult }));

        ResponseEntity<CrimeLocationResponse> expected = ResponseEntity.ok(new CrimeLocationResponse(new Crime[]{crime}));
        ResponseEntity<CrimeLocationResponse> actual = crimeLocationService.getCrimesAtPostcode(postcode);

        assertThat(actual, sameBeanAs(expected));
    }

    @Test
    void getCrimesAtPostcode_postcodeError() {
        when(mockPostcodeClient.getPostcodeResult(postcode))
                .thenReturn(Either.left(new HttpError(HttpStatus.NOT_FOUND, "postcode error message")));

        ResponseEntity<CrimeLocationResponse> expected = ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CrimeLocationResponse("postcode error message"));
        ResponseEntity<CrimeLocationResponse> actual = crimeLocationService.getCrimesAtPostcode(postcode);

        assertThat(actual, sameBeanAs(expected));
        verifyNoInteractions(mockCrimesClient);
    }

    @Test
    void getCrimesAtPostcode_crimeError() {
        when(mockPostcodeClient.getPostcodeResult(postcode))
                .thenReturn(Either.right(postCodeResult));
        when(mockCrimesClient.getCrimes(postCodeResult))
                .thenReturn(Either.left(new HttpError(HttpStatus.BAD_GATEWAY, "crime error message")));

        ResponseEntity<CrimeLocationResponse> expected = ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new CrimeLocationResponse("crime error message"));
        ResponseEntity<CrimeLocationResponse> actual = crimeLocationService.getCrimesAtPostcode(postcode);

        assertThat(actual, sameBeanAs(expected));
    }


    String postcode = "S71GQ";
    PostCodeResult postCodeResult = new PostCodeResult(52f, -1f);
    CrimeResult crimeResult = CrimeResult.builder()
            .category("violent-crime")
            .location_type("Force")
            .location(CrimeLocation.builder()
                    .latitude("52")
                    .longitude("-1")
                    .street(new CrimeStreet(884227, "On or near Abbey Gate"))
                    .build())
            .context("")
            .outcome_status(new CrimeOutcome("Unable to prosecute suspect", "2017-02"))
            .persistent_id("4d83433f3117b3a4d2c80510c69ea188a145bd7e94f3e98924109e70333ff735")
            .id(54726925)
            .location_subtype("")
            .month("2017-02")
            .build();
    Crime crime = Crime.builder()
            .category("violent-crime")
            .month("2017-02")
            .outcome("Unable to prosecute suspect")
            .street("On or near Abbey Gate")
            .build();

}