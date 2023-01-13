package com.github.tomaszgryczka.securenotes.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class KeycloakUserController {

    private final KeycloakUserService keycloakUserService;

    @GetMapping("/all")
    public List<KeycloakUser> getAllUsers() {
        return keycloakUserService.getAllUsers();
    }

    @GetMapping("/current")
    public KeycloakUser getCurrentUser() {
        return keycloakUserService.getKeycloakUserInfoFromJwt();
    }
}
