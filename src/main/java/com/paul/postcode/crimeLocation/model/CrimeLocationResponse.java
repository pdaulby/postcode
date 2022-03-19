package com.paul.postcode.crimeLocation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class CrimeLocationResponse {
    Crime[] crimes;
    String errorMessage;

    public CrimeLocationResponse(Crime[] crimes){
        this.crimes = crimes;
    }
    public CrimeLocationResponse(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
