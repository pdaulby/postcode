package com.paul.postcode.tinytype;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class HttpError {
    HttpStatus status;
    String message;
}
