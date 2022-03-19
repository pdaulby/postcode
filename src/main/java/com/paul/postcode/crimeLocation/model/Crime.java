package com.paul.postcode.crimeLocation.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Crime {
    String category;
    String street;
    String outcome;
    String month; //"2017-02"
}
