package com.bhanna.happyhour.controller;

import com.bhanna.happyhour.model.SpecialDetail;
import com.bhanna.happyhour.service.SpecialDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/specialdetail")
public class SpecialDetailController {

    private final SpecialDetailService specialDetailService;

    @Autowired
    public SpecialDetailController(SpecialDetailService specialDetailService) {
        this.specialDetailService = specialDetailService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialDetail> getSpecialDetail(@PathVariable UUID id) {
        SpecialDetail specialDetail = specialDetailService.getSpecialDetail(id);
        return ResponseEntity.ok(specialDetail);
    }

    @GetMapping
    public ResponseEntity<List<SpecialDetail>> getAllSpecialDetails() {
        List<SpecialDetail> specialDetailList = specialDetailService.getAllSpecialDetails();
        return ResponseEntity.ok(specialDetailList);
    }
}
