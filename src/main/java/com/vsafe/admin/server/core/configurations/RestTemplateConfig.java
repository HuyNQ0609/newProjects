package com.vsafe.admin.server.core.configurations;

import com.vsafe.admin.server.core.configurations.properties.RestTemplateProperties;
import com.vsafe.admin.server.helpers.utils.AssertUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(properties.getConnectTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .build();
    }

    private final RestTemplateProperties properties;
    public RestTemplateConfig(RestTemplateProperties properties) {
        AssertUtils.defaultNotNull(properties);
        this.properties = properties;
    }
}
