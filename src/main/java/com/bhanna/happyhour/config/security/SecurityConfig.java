package com.bhanna.happyhour.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final SecurityConfigProperties securityConfigProperties;

    @Autowired
    public SecurityConfig(SecurityConfigProperties securityConfigProperties) {
        this.securityConfigProperties = securityConfigProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // must be explicitly added before security to do CORS check first
                // by default uses a Bean by the name of CorsConfigurationSource
                .cors(withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").authenticated()
                        .requestMatchers("/api/v1/business").hasAuthority("SCOPE_create:business")
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(makeJwtDecoder())
                        )
                );

        return http.build();
    }

    /**
     * By default, Spring Security does not validate the "aud" claim of the token, to ensure that this token is
     * indeed intended for our app. Adding our own validator is easy to do:
     */
    private JwtDecoder makeJwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(securityConfigProperties.getIssuerUri());

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(securityConfigProperties.getAudience());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(securityConfigProperties.getIssuerUri());
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }
}
