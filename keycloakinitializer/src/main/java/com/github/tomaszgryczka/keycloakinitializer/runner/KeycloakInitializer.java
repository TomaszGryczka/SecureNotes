package com.github.tomaszgryczka.keycloakinitializer.runner;

import com.github.tomaszgryczka.keycloakinitializer.config.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakInitializer implements CommandLineRunner {

    private final KeycloakProperties keycloakProperties;
    private final Keycloak keycloakAdmin;

    @Override
    public void run(String... args) {
        final String realmName = keycloakProperties.getRealmName();

        log.info("Initializing {} realm in Keycloak...", realmName);
        deleteRealmConfigIfExists(realmName);

        final RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(realmName);
        realmRepresentation.setEnabled(true);
        realmRepresentation.setRegistrationAllowed(true);

        final List<ClientRepresentation> clients = keycloakProperties.getClients().stream()
                .map(client -> {
                    ClientRepresentation clientRepresentation = new ClientRepresentation();
                    clientRepresentation.setClientId(client.getId());
                    clientRepresentation.setDirectAccessGrantsEnabled(true);
                    clientRepresentation.setPublicClient(true);
                    clientRepresentation.setRedirectUris(Collections.singletonList(client.getRedirectUrl()));

                    return clientRepresentation;
                }).toList();
        realmRepresentation.setClients(clients);


        final List<UserRepresentation> userRepresentations = keycloakProperties.getUsers().stream()
                .map(user -> {
                    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                    credentialRepresentation.setValue(user.getPassword());

                    UserRepresentation userRepresentation = new UserRepresentation();
                    userRepresentation.setUsername(user.getUsername());
                    userRepresentation.setEnabled(true);
                    userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
                    Map<String, List<String>> clientRoles = keycloakProperties.getClients().stream()
                            .map(KeycloakProperties.Client::getId)
                            .collect(Collectors.toMap(k -> k, v -> user.getRoles()));
                    userRepresentation.setClientRoles(clientRoles);

                    return userRepresentation;
                }).toList();
        realmRepresentation.setUsers(userRepresentations);

        keycloakAdmin.realms().create(realmRepresentation);

        log.info("Realm {} initialized successfully in Keycloak!", realmName);
    }

    private void deleteRealmConfigIfExists(final String realmName) {
        keycloakAdmin.realms()
                .findAll()
                .stream()
                .filter(realm -> realm.getRealm().equals(realmName))
                .findAny()
                .ifPresent(realm -> keycloakAdmin.realm(realmName).remove());
    }
}
