package com.paul.postcode.crimeLocation.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
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
