package com.infinibet.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Getter
public class ApplicationProperties {
    @Value("${app.security.jwtSecret}")
    private String jwtSecret;

    @Value("${app.security.jwtBase64Secret}")
    private String jwtBase64Secret;

    @Value("${app.security.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Value("${app.security.jwtExpirationInMsForRememberMe}")
    private long jwtExpirationInMsForRememberMe;
}
