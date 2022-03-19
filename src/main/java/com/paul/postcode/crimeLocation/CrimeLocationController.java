package com.paul.postcode.crimeLocation;

import com.paul.postcode.crimeLocation.model.CrimeLocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrimeLocationController {
    @Autowired
    private CrimeLocationService service;

    @GetMapping("/crimes-at-postcode/{postcode}")
    ResponseEntity<CrimeLocationResponse> getCrimesAtPostcode(@PathVariable String postcode){ //would be good to validate postcode here
        return service.getCrimesAtPostcode(postcode);
    }
}
