package com.bhanna.happyhour.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBusinessRequest {
    private String yelpId;
    private String businessName;
    private String businessUrl;
}
