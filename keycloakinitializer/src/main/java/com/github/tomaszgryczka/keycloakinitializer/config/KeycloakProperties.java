package com.github.tomaszgryczka.keycloakinitializer.config;

import com.github.tomaszgryczka.keycloakinitializer.security.Role;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String serverUrl;
    private String realmName;
    private List<Client> clients;
    private List<User> users;

    @Data
    public static class Client {

        private String id;
        private String redirectUrl;
    }

    @Data
    public static class User {

        private String username;
        private String password;
        private List<Role> roles;

        public List<String> getRoles() {
            return roles.stream()
                    .map(Role::toString)
                    .toList();
        }
    }
}
