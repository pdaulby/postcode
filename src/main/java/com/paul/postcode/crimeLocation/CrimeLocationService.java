package com.paul.postcode.crimeLocation;

import com.paul.postcode.client.CrimesClient;
import com.paul.postcode.client.PostCodeClient;
import com.paul.postcode.client.model.PostCodeResult;
import com.paul.postcode.crimeLocation.model.CrimeLocationResponse;
import com.paul.postcode.crimeLocation.model.CrimeLocationResponseMapper;
import com.paul.postcode.tinytype.HttpError;
import io.vavr.control.Either;
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
        Either<HttpError, PostCodeResult> postcodeResult = postCodeClient.getPostcodeResult(postcode);
        if (postcodeResult.isEmpty()) {
            return errorToEntity(postcodeResult.getLeft());
        }
        return crimesClient.getCrimes(postcodeResult.get())
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
