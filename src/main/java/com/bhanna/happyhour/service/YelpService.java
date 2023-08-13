package com.bhanna.happyhour.service;

import com.bhanna.happyhour.client.YelpClient;
import com.bhanna.happyhour.model.Business;
import com.bhanna.happyhour.model.CompositeBusinessDto;
import com.bhanna.happyhour.model.YelpBusiness;
import com.bhanna.happyhour.model.YelpBusinessesSearchResponse;
import com.bhanna.happyhour.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class YelpService {

    private final YelpClient yelpClient;
    private final BusinessRepository businessRepository;

    @Autowired
    public YelpService(YelpClient yelpClient, BusinessRepository businessRepository) {
        this.yelpClient = yelpClient;
        this.businessRepository = businessRepository;
    }

    public List<CompositeBusinessDto> getBusinessesFromYelp(String location, String search) {
        List<YelpBusiness> yelpBusinesses = getYelpBusinesses(location, search).getBusinesses();
        List<String> yelpIds = yelpBusinesses.stream().map(YelpBusiness::getId).collect(Collectors.toList());
        List<Business> businesses = businessRepository.findByYelpIdIn(yelpIds);
        return yelpBusinesses.stream().map(yelpBusiness -> new CompositeBusinessDto(
                businesses.stream()
                        .filter(business -> Objects.equals(business.getYelpId(), yelpBusiness.getId()))
                        .findFirst().orElse(null),
                yelpBusiness
        )).collect(Collectors.toList());
    }

    public YelpBusinessesSearchResponse getYelpBusinesses(String location, String search) {
        return yelpClient.getYelpBusinesses(location, search);
    }
}
