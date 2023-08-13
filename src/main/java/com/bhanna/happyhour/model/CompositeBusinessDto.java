package com.bhanna.happyhour.model;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompositeBusinessDto {
    @Nullable
    private Business business;
    private YelpBusiness yelpBusiness;
}
