package com.paul.postcode.crimeLocation.model;

import com.paul.postcode.client.model.CrimeResult;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CrimeLocationResponseMapper {
    public CrimeLocationResponse mapLocationResponse(CrimeResult[] results) {
        Crime[] crimes = Arrays.stream(results)
                .map(this::ToCrime)
                .toArray(Crime[]::new);
        return new CrimeLocationResponse(crimes);
    }

    private Crime ToCrime(CrimeResult crimeResult) {
        return Crime.builder()
                .category(crimeResult.getCategory())
                .street(crimeResult.getLocation().getStreet().getName())
                .outcome(crimeResult.getOutcome_status().getCategory())
                .month(crimeResult.getMonth())
                .build();
    }
}
