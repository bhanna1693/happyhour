package com.bhanna.happyhour.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiResponse {
    private List<SpecialDetail> specialDetails;
}
