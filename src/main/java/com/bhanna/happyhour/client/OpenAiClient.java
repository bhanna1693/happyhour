package com.bhanna.happyhour.client;

import com.bhanna.happyhour.model.OpenAiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OpenAiClient {

    @Value("${happyhour.openai.clientUrl}")
    private String clientUrl;

    private WebClient webClient() {
        return WebClient.builder()
                .baseUrl(clientUrl)
                .defaultHeaders(headers -> {
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//                    headers.setBearerAuth(token);
                })
                .build();
    }

    public OpenAiResponse getOpenAiResponse(String csv) {
        return webClient()
                .post()
                .body(BodyInserters.fromValue(csv))
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(OpenAiResponse.class);
                    } else {
                        return response.createException()
                                .flatMap(Mono::error);
                    }
                })
//                TODO: remove blocking operation
                .block();
    }
}
