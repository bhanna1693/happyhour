package com.bhanna.happyhour.service;

import com.bhanna.happyhour.model.SpecialDetail;
import com.bhanna.happyhour.repository.SpecialDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpecialDetailService {
    private final SpecialDetailsRepository specialDetailsRepository;

    @Autowired
    public SpecialDetailService(SpecialDetailsRepository specialDetailsRepository) {
        this.specialDetailsRepository = specialDetailsRepository;
    }

    public SpecialDetail getSpecialDetail(UUID id) {
        Optional<SpecialDetail> specialDetail = specialDetailsRepository.findById(id);
        return specialDetail.orElseThrow(() -> new EntityNotFoundException("SpecialDetail not found for id: " + id));
    }

    public List<SpecialDetail> getAllSpecialDetails() {
        return specialDetailsRepository.findAll();
    }

}
