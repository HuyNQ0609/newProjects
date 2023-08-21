package com.vsafe.admin.server.core.security.custom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

@Component
public class CorsConfigurationSource implements org.springframework.web.cors.CorsConfigurationSource {

    @Value("${web.cors.allowed-origins:*}")
    private String ALLOWED_ORIGIN;

    @Value("${web.cors.allowed-method:POST,GET,OPTIONS,PUT,PATCH,DELETE}")
    private String[] ALLOWED_METHOD;

    @Value("${web.cors.allow-credentials:true}")
    private boolean ALLOW_CREDENTIALS;

    @Value("${web.cors.allowed-headers:*}")
    private String ALLOWED_HEADER;

    @Value("${web.cors.max-age:3600}")
    private Long MAX_AGE;

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOriginPatterns(Collections.singletonList(ALLOWED_ORIGIN));
        cors.setAllowedMethods(Arrays.asList(ALLOWED_METHOD));
        cors.setAllowCredentials(ALLOW_CREDENTIALS);
        cors.setAllowedHeaders(Collections.singletonList(ALLOWED_HEADER));
        cors.setMaxAge(MAX_AGE);
        return cors;
    }
}
