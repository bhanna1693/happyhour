package com.bhanna.happyhour.service;

import com.bhanna.happyhour.client.OpenAiClient;
import com.bhanna.happyhour.model.OpenAiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OpenAiService {
    private final OpenAiClient openAiClient;

    @Autowired
    public OpenAiService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    public OpenAiResponse getOpenAiResponse(String csv) {
        try {
            log.info("Fetching OpenAI response...");
            OpenAiResponse openAiResponse = openAiClient.getOpenAiResponse(csv);
            log.info("OpenAI response retrieved");
            return openAiResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
