package com.github.tomaszgryczka.securenotes.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakAdminConfig {
    private static final String MASTER_REALM_NAME = "master";
    private static final String CLIENT_ID = "admin-cli";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Bean
    public Keycloak admin() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(MASTER_REALM_NAME)
                .username(ADMIN_USERNAME)
                .password(ADMIN_PASSWORD)
                .clientId(CLIENT_ID)
                .build();
    }
}
