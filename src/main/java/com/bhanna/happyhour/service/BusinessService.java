package com.bhanna.happyhour.service;

import com.bhanna.happyhour.model.*;
import com.bhanna.happyhour.repository.BusinessRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Log4j2
public class BusinessService {
    private final BusinessRepository businessRepository;
    private final AsyncBusinessService asyncBusinessService;

    @Autowired
    public BusinessService(
            BusinessRepository businessRepository, AsyncBusinessService asyncBusinessService) {
        this.businessRepository = businessRepository;
        this.asyncBusinessService = asyncBusinessService;
    }

    public Business createBusiness(CreateBusinessRequest createBusinessRequest) {
        Optional<Business> optionalBusiness = businessRepository.findBusinessByYelpId(createBusinessRequest.getYelpId());

        if (optionalBusiness.isEmpty()) {
            log.info("Business not found. Creating new business: [business:{}, yelpId:{}]", createBusinessRequest.getBusinessName(), createBusinessRequest.getYelpId());
            Business business = businessRepository.save(BusinessMapper.mapFromYelpBusiness(createBusinessRequest));
            log.info("Saved new business: [business:{}, businessId:{}]", business.getName(), business.getId());
            return business;
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Business already exists.");
        }
    }

    public Business updateBusinessSpecials(Long id, UpdateBusinessSpecialsRequest request) {
        Business business = getBusiness(id, true);
        validateRequest(business);

        business.setWebsite(request.getBusinessUrl());
        Business updatedBusiness = updateSpecialCheckStatus(business, SpecialCheckStatus.PENDING);

        // has not checked for specials today
        asyncBusinessService.asyncProcessBusinessWithWebScraper(business);

        // return business with PENDING status
        log.info("RESPONDING WHILE ASYNC PROCESS IS WORKING IN BACKGROUND");
        return updatedBusiness;
    }

    private Business updateSpecialCheckStatus(Business business, SpecialCheckStatus specialCheckStatus) {
        business.setLatestSpecialsCheck(LocalDateTime.now()); // do I need this??
        business.setSpecialCheckStatus(specialCheckStatus);
        return businessRepository.save(business);
    }

    private void validateRequest(Business business) {
        switch (business.getSpecialCheckStatus()) {
            case FAILED, COMPLETED -> {
                if (hasCheckedForSpecialsToday(business)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Business already scanned for specials today.");
                }
            }
            case PENDING -> {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Business is currently getting specials");
            }
            case INITIAL -> {
            }
        }
    }

    private boolean hasCheckedForSpecialsToday(Business business) {
        return business.getLatestSpecialsCheck() != null && business.getLatestSpecialsCheck().toLocalDate().isEqual(LocalDate.now());
    }

    private Business getBusinessLazy(Long id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Business not found for id: " + id));
    }

    private Business getBusinessAndSpecialDetailsEagerly(Long id) {
        return businessRepository.findBusinessAndSpecialDetailsEagerly(id)
                .orElseThrow(() -> new EntityNotFoundException("Business not found for id: " + id));
    }

    private Business getBusinessLazy(String yelpId) {
        return businessRepository.findBusinessByYelpId(yelpId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found for yelpId: " + yelpId));
    }

    private Business getBusinessAndSpecialDetailsEagerly(String yelpId) {
        return businessRepository.findBusinessAndSpecialDetailsByYelpIdEagerly(yelpId)
                .orElseThrow(() -> new EntityNotFoundException("Business not found for yelpId: " + yelpId));
    }

    public Business getBusiness(Long id, Boolean includeSpecials) {
        if (includeSpecials) {
            return getBusinessAndSpecialDetailsEagerly(id);
        } else {
            return getBusinessLazy(id);
        }
    }

    public Business getBusinessByYelpId(String yelpId, Boolean includeSpecials) {
        if (includeSpecials) {
            return getBusinessAndSpecialDetailsEagerly(yelpId);
        } else {
            return getBusinessLazy(yelpId);
        }
    }
}
