package com.paul.postcode.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrimeResult {
        String category;
        String location_type;
        CrimeLocation location;
        String context;
        CrimeOutcome outcome_status;
        String persistent_id;
        int id;
        String location_subtype;
        String month; //2017-02
}
