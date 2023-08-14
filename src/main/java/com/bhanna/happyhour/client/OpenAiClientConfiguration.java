package com.bhanna.happyhour.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "happyhour.openai")
@Getter
@Setter
public class OpenAiClientConfiguration {
    private String url;
}
