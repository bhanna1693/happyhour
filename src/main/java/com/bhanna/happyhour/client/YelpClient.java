package com.bhanna.happyhour.client;

import com.bhanna.happyhour.model.YelpBusinessesSearchResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Log4j2
public class YelpClient {

    private final WebClient webClient;

    @Autowired
    public YelpClient(YelpClientConfiguration yelpClientConfiguration) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.yelp.com/v3")
                .defaultHeaders(headers -> {
                    headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    headers.setBearerAuth(yelpClientConfiguration.getToken());
                })
                .build();
    }

    public YelpBusinessesSearchResponse getYelpBusinesses(String location, String search) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/businesses/search")
                        .queryParam("location", location)
                        .queryParam("term", search)
                        .queryParam("sort_by", "best_match")
                        .queryParam("limit", 20)
                        .build()
                )
                .retrieve()
                .bodyToMono(YelpBusinessesSearchResponse.class)
                .block();
    }

}

