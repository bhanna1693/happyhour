package com.bhanna.happyhour.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.jwt")
public class SecurityConfigProperties {

    private String audience;
    private String issuerUri;
}