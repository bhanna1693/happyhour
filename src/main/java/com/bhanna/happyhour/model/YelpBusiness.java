package com.bhanna.happyhour.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YelpBusiness {

    private String id;
    private String alias;
    private String name;
    private String image_url;
    private Boolean is_closed;
    private String url;
    private Integer review_count;
    private List<Category> categories;
    private Double rating;
    private Coordinates coordinates;
    private List<String> transactions;
    private String price;
    private Location location;
    private String phone;
    private String display_phone;
    private Double distance;

    @Data
    public static class Category {
        private String alias;
        private String title;

    }

    @Data
    public static class Coordinates {
        private Double latitude;
        private Double longitude;

    }

    @Data
    public static class Location {
        private String address1;
        private String address2;
        private String address3;
        private String city;
        private String zip_code;
        private String country;
        private String state;
        private List<String> display_address;

    }

}
