package com.vsafe.admin.server.core.configurations.dbConfig.mongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ConfigurationProperties(prefix = "cms.mongodb")
@EnableMongoRepositories
        (basePackages = {"com.vsafe.admin.server.business.repositories.cms"},
                mongoTemplateRef = CMSConfig.MONGO_TEMPLATE)
@Slf4j
public class CMSConfig extends AbstractMongoDbConfig {
    public static final String MONGO_TEMPLATE = "cmsMongoTemplate";

    @Override
    @Primary
    @Bean(name = MONGO_TEMPLATE)
    public MongoTemplate getMongoTemplate() throws Exception {
        log.info("Creating MongoTemplate for CMS DB");
        return new MongoTemplate(mongoDbFactory());
    }

}
