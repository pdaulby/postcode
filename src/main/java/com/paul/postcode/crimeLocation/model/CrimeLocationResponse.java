package com.paul.postcode.crimeLocation.model;

import lombok.Data;

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
