package com.github.tomaszgryczka.securenotes.note;

import com.github.tomaszgryczka.securenotes.keycloak.KeycloakUser;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NoteRequest {
    private String password;
    private String content;

    private NoteStatus status;
    private List<KeycloakUser> sharedToUsers;
}
