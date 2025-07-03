package com.example.configserver.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.environment.ConfigTokenProvider;
import org.springframework.cloud.config.server.environment.VaultEnvironmentProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(VaultEnvironmentProperties.class)
public class VaultConfig {

    @Primary
    @Bean
    public ConfigTokenProvider configTokenProvider(VaultEnvironmentProperties vaultEnvironmentProperties) {
        return () -> "root"; // use your static token
    }

}

