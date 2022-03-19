package com.paul.postcode.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrimeLocation  {
    String latitude; //"52.643950"
    CrimeStreet street;
    String longitude; //"-1.143042"
}