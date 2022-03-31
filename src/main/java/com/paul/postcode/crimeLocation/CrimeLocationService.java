package com.paul.postcode.crimeLocation;

import com.paul.postcode.client.CrimesClient;
import com.paul.postcode.client.PostCodeClient;
import com.paul.postcode.crimeLocation.model.CrimeLocationResponse;
import com.paul.postcode.crimeLocation.model.CrimeLocationResponseMapper;
import com.paul.postcode.tinytype.HttpError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CrimeLocationService {
    @Autowired
    private CrimesClient crimesClient;
    @Autowired
    private PostCodeClient postCodeClient;
    @Autowired
    private CrimeLocationResponseMapper responseMapper;

    public ResponseEntity<CrimeLocationResponse> getCrimesAtPostcode(String postcode) {
        return postCodeClient.getPostcodeResult(postcode)
                .flatMap(crimesClient::getCrimes)
                .map(responseMapper::mapLocationResponse)
                .map(ResponseEntity::ok)
                .getOrElseGet(this::errorToEntity);
    }

    private ResponseEntity<CrimeLocationResponse> errorToEntity(HttpError error) {
        return ResponseEntity
                .status(error.getStatus())
                .body(new CrimeLocationResponse(error.getMessage()));
    }
}
