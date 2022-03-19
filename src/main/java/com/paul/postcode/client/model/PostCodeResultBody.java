package com.paul.postcode.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCodeResultBody {
    private String status;
    private PostCodeResult result;
}
