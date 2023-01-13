package com.github.tomaszgryczka.securenotes.keycloak;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeycloakUser {
    private String id;
    private String username;
}