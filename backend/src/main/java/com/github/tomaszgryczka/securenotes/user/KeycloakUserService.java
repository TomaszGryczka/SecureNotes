package com.github.tomaszgryczka.securenotes.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final Keycloak keycloakAdmin;
    private static final String USER_ID = "sub";
    private static final String USERNAME = "preferred_username";
    private static final String REALM = "app";

    public KeycloakUser getKeycloakUserInfoFromJwt() {
        final JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        final Jwt token = authenticationToken.getToken();

        return KeycloakUser.builder()
                .id(token.getClaim(USER_ID))
                .username(token.getClaim(USERNAME))
                .build();
    }

    public List<KeycloakUser> getAllUsers() {
        final List<UserRepresentation> userRepresentations = keycloakAdmin.realm(REALM).users().list();
        final String currentUserId = getKeycloakUserInfoFromJwt().getId();

        return userRepresentations.stream()
                .filter(userRepresentation -> !Objects.equals(userRepresentation.getId(), currentUserId))
                .map(userRepresentation -> KeycloakUser.builder()
                        .id(userRepresentation.getId())
                        .username(userRepresentation.getUsername())
                        .build()).collect(Collectors.toList());
    }
}
