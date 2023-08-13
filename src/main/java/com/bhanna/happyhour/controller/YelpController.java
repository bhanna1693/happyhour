package com.bhanna.happyhour.controller;

import com.bhanna.happyhour.model.CompositeBusinessDto;
import com.bhanna.happyhour.service.YelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/yelp")
public class YelpController {

    private final YelpService yelpService;

    @Autowired
    public YelpController(YelpService yelpService) {
        this.yelpService = yelpService;
    }


    @GetMapping("/businesses/search")
    public ResponseEntity<List<CompositeBusinessDto>> yelpBusinessSearch(@RequestParam String location, @RequestParam String search) {
        List<CompositeBusinessDto> businessList = yelpService.getBusinessesFromYelp(location, search);
        return ResponseEntity.ok(businessList);
    }
}
