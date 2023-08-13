package com.bhanna.happyhour.model;

public class BusinessMapper {

    public static Business mapFromYelpBusiness(CreateBusinessRequest createBusinessRequest) {
        Business business = new Business();
        business.setName(createBusinessRequest.getBusinessName());
        business.setWebsite(createBusinessRequest.getBusinessUrl());
        business.setYelpId(createBusinessRequest.getYelpId());
        return business;
    }
}

