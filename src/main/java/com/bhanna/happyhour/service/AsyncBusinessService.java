package com.bhanna.happyhour.service;

import com.bhanna.happyhour.model.Business;
import com.bhanna.happyhour.model.OpenAiResponse;
import com.bhanna.happyhour.model.SpecialCheckStatus;
import com.bhanna.happyhour.repository.BusinessRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@Log4j2
public class AsyncBusinessService {

    private final BusinessRepository businessRepository;
    private final WebScraperService webScraperService;
    private final OpenAiService openAiService;

    @Autowired
    public AsyncBusinessService(BusinessRepository businessRepository, WebScraperService webScraperService, OpenAiService openAiService) {
        this.businessRepository = businessRepository;
        this.webScraperService = webScraperService;
        this.openAiService = openAiService;
    }

    //    aysnc method must be in its own service class to work properly with spring
    @Async("threadPoolTaskExecutor")
    public void asyncProcessBusinessWithWebScraper(Business business) {
        try {
            log.info("Processing business with web scraper and OpenAI: [business:{}, businessId:{}]", business.getName(), business.getId());
            String csv = webScraperService.scrapeWebsite(business.getWebsite());
            OpenAiResponse openAiResponse = openAiService.getOpenAiResponse(csv);
            Business updatedBusiness = updateBusinessFromOpenAiResponse(business, openAiResponse);
            updateSpecialCheckStatus(business, SpecialCheckStatus.COMPLETED);
            log.info("Successfully processed business with web scraper and OpenAI: [business:{}, businessId:{}]", updatedBusiness.getName(), updatedBusiness.getId());
        } catch (Exception e) {
            log.error("Failed to process business with web scraper and OpenAI [business:{}, businessId:{}]", business.getName(), business.getId(), e);
            updateSpecialCheckStatus(business, SpecialCheckStatus.FAILED);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    private Business updateBusinessFromOpenAiResponse(Business business, OpenAiResponse openAiResponse) {
        try {
            log.info("Updating business from OpenAI Response: [business:{}, businessId:{}]", business.getName(), business.getId());
            openAiResponse.getSpecialDetails().forEach(specialDetail -> {
                specialDetail.setBusiness(business);
            });
            business.getSpecialDetailList().clear();
            business.getSpecialDetailList().addAll(openAiResponse.getSpecialDetails());
            Business updatedBusiness = businessRepository.save(business);
            log.info("Successfully updated business from OpenAI Response: [business:{}, businessId:{}]", updatedBusiness.getName(), updatedBusiness.getId());
            return updatedBusiness;
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage(), e);
            business.getSpecialDetailList().clear();
            throw new RuntimeException("Failed to save business with special details", e);
        } catch (Exception e) {
            log.error("Failed to save business with special details");
            throw new RuntimeException("Failed to save business with special details", e);
        }
    }

    private Business updateSpecialCheckStatus(Business business, SpecialCheckStatus specialCheckStatus) {
        business.setLatestSpecialsCheck(LocalDateTime.now()); // do I need this??
        business.setSpecialCheckStatus(specialCheckStatus);
        return businessRepository.save(business);
    }
}
