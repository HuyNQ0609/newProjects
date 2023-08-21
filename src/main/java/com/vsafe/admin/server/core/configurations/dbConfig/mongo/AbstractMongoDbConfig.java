package com.vsafe.admin.server.core.configurations.dbConfig.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.event.ConnectionPoolCreatedEvent;
import com.mongodb.event.ConnectionPoolListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
@PropertySources({@PropertySource("classpath:databases.properties")})
public abstract class AbstractMongoDbConfig {

    public static final int MAX_SIZE = 10;
    public static final int MIN_SIZE = 2;
    public static final int MAX_CONNECT_IDLE_TIME = 10;
    public static final int MAX_WAIT_TIME = 2;
    public static final int MAX_CONNECTION_LIFE_TIME = 10;
    private String host;
    private String username;
    private String password;
    private String database;

    private String authenticationDatabase;
    private int port;

    private int maxSize = MAX_SIZE;
    private int minSize = MIN_SIZE;
    private int maxConnectIdleTime = MAX_CONNECT_IDLE_TIME;
    private int maxWaitTime = MAX_WAIT_TIME;
    private int maxConnectionLifeTime = MAX_CONNECTION_LIFE_TIME;

    public MongoDatabaseFactory mongoDbFactory() {
        MongoCredential mongoCredential = MongoCredential.createCredential(username, authenticationDatabase, password.toCharArray());
        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder().
                        applyToClusterSettings(
                                builder -> builder
                                        .hosts(Collections.singletonList(new ServerAddress(host, port))))
                        .applyToConnectionPoolSettings(
                                this::applyConnectPoolSetting
                        )
                        .credential(mongoCredential).build()
        );

        return new SimpleMongoClientDatabaseFactory(mongoClient, database);
    }

    public void applyConnectPoolSetting(ConnectionPoolSettings.Builder builder) {
        builder.maxSize(maxSize)
                .minSize(minSize)
                .maxConnectionIdleTime(maxConnectIdleTime, TimeUnit.MINUTES)
                .maxWaitTime(maxWaitTime, TimeUnit.MINUTES)
                .maxConnectionLifeTime(maxConnectionLifeTime, TimeUnit.MINUTES)
                .addConnectionPoolListener(new MongoConnectionPoolListener());
    }

    public abstract MongoTemplate getMongoTemplate() throws Exception;

}
