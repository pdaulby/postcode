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
        String street = crimeResult.getLocation() == null ? null
                : crimeResult.getLocation().getStreet() == null ? null
                : crimeResult.getLocation().getStreet().getName();
        String outcome =  crimeResult.getOutcome_status() == null ? null
                : crimeResult.getOutcome_status().getCategory();
        return Crime.builder()
                .category(crimeResult.getCategory())
                .street(street)
                .outcome(outcome)
                .month(crimeResult.getMonth())
                .build();
    }
}
