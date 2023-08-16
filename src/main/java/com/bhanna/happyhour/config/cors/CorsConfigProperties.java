package com.bhanna.happyhour.config.cors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "happyhour.cors")
public class CorsConfigProperties {
    private List<String> allowedOrigins;
}
