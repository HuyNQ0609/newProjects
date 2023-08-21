package com.vsafe.admin.server.core.configurations.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth")
@Data
public class OAuth2Properties {
    private String resourceId;
    private String jwtSingingKey;

    private String clientId;

    private String clientSecret;

    private UrlConnect url;

    @Data
    public static class UrlConnect {
        private String login;
        private String logout;
        private String checkToken;
        private String changePassword;
        private String addUser;
        private String updateUsername;
    }
}
