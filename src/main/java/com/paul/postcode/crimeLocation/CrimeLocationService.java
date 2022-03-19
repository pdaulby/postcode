package com.paul.postcode.crimeLocation;

import com.paul.postcode.client.CrimesClient;
import com.paul.postcode.client.PostCodeClient;
import com.paul.postcode.client.model.PostCodeResult;
import com.paul.postcode.crimeLocation.model.CrimeLocationResponse;
import com.paul.postcode.crimeLocation.model.CrimeLocationResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CrimeLocationService {
    @Autowired
    private CrimesClient crimesClient;
    @Autowired
    private PostCodeClient postCodeClient;
    @Autowired
    private CrimeLocationResponseMapper responseMapper;

    public ResponseEntity<CrimeLocationResponse> getCrimesAtPostcode(String postcode) {
        Optional<PostCodeResult> postcodeResult = postCodeClient.getPostcodeResult(postcode);
        if (postcodeResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CrimeLocationResponse("Postcode Not found"));
        }
        return crimesClient.getCrimes(postcodeResult.get())
                .map(responseMapper::mapLocationResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CrimeLocationResponse("Could not access crime data")));
    }
}
