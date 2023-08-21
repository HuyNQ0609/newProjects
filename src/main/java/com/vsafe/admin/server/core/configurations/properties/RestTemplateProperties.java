package com.vsafe.admin.server.core.configurations.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest-template.config")
@Data
public class RestTemplateProperties {
    private int connectRequestTimeout;
    private int connectTimeout;
    private int readTimeout;
}
