package com.bhanna.happyhour.client;

import com.bhanna.happyhour.model.YelpBusinessesSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class YelpClient {
    private final WebClient webClient;

    @Value("${happyhour.yelp.token}")
    private String token;

    public YelpClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.yelp.com/v3")
                .defaultHeaders(headers -> {
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setBearerAuth(token);
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
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(YelpBusinessesSearchResponse.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
//                TODO: remove blocking operation
                .block();
    }
}
