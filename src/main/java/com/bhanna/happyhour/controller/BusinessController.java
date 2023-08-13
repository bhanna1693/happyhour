package com.bhanna.happyhour.controller;

import com.bhanna.happyhour.model.Business;
import com.bhanna.happyhour.model.CreateBusinessRequest;
import com.bhanna.happyhour.model.UpdateBusinessSpecialsRequest;
import com.bhanna.happyhour.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/business")
public class BusinessController {

    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping
    public ResponseEntity<Business> createBusiness(@RequestBody CreateBusinessRequest createBusinessRequest) {
        Business business = businessService.createBusiness(createBusinessRequest);
        return new ResponseEntity<>(business, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Business> updateBusinessSpecials(@PathVariable Long id, @RequestBody UpdateBusinessSpecialsRequest request) {
        Business updatedBusinessPending = businessService.updateBusinessSpecials(id, request);
        return new ResponseEntity<>(updatedBusinessPending, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{yelpId}")
    public ResponseEntity<Business> getBusinessByYelpId(@PathVariable String yelpId, @RequestParam(defaultValue = "false") Boolean includeSpecials) {
        Business business = businessService.getBusinessByYelpId(yelpId, includeSpecials);
        return ResponseEntity.ok(business);
    }

}
