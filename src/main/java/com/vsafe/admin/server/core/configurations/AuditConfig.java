package com.vsafe.admin.server.core.configurations;

import com.vsafe.admin.server.helpers.utils.AssertUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableMongoAuditing
public class AuditConfig extends AbstractMongoClientConfiguration {

    private final TokenStore tokenStore;

    public AuditConfig(TokenStore tokenStore) {
        AssertUtils.defaultNotNull(tokenStore);
        this.tokenStore = tokenStore;
    }

    @Override
    protected String getDatabaseName() {
        return "db-cms";
    }

    @Bean
    public AuditorAware<String> vsafeAuditorProvider() {
        return new AuditorAwareImpl(tokenStore);
    }
}
