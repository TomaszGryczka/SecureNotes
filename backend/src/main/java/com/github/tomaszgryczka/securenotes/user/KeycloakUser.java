package com.github.tomaszgryczka.securenotes.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeycloakUser {
    private String id;
    private String username;
}