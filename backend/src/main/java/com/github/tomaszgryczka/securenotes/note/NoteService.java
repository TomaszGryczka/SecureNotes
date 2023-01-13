package com.github.tomaszgryczka.securenotes.note;

import com.github.tomaszgryczka.securenotes.user.AppUser;
import com.github.tomaszgryczka.securenotes.user.AppUserRepository;
import com.github.tomaszgryczka.securenotes.keycloak.KeycloakUser;
import com.github.tomaszgryczka.securenotes.keycloak.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final KeycloakUserService keycloakUserService;
    private final AppUserRepository appUserRepository;

    public Note saveNote(final NoteRequest noteRequest) {
        final KeycloakUser noteOwner = keycloakUserService.getKeycloakUserInfoFromJwt();
        final List<AppUser> usersWithAccessToNote =
                getUsersThatWillHaveAccessToNote(noteOwner, noteRequest.getSharedToUsers());

        final Note noteToAdd = Note.builder()
                .noteStatus(noteRequest.getStatus())
                .password(noteRequest.getPassword())
                .content(noteRequest.getContent())
                .users(usersWithAccessToNote)
                .build();

        usersWithAccessToNote.forEach(user -> user.getNotes().add(noteToAdd));

        return noteRepository.save(noteToAdd);
    }

    public Note getNoteByNoteId(final Long noteId) {
        return noteRepository.findById(noteId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    }

    public List<BasicNoteResponse> getUserNotes() {
        final KeycloakUser noteOwner = keycloakUserService.getKeycloakUserInfoFromJwt();

        return noteRepository.getUsersNotes(noteOwner.getId()).stream()
                .map(note ->
                        BasicNoteResponse.builder()
                                .id(note.getId())
                                .status(note.getNoteStatus())
                                .build())
                .collect(Collectors.toList());
    }

    private List<AppUser> getUsersThatWillHaveAccessToNote(final KeycloakUser noteOwner,
                                                           final List<KeycloakUser> usersWithAccessToNote) {

        final List<AppUser> appUsers = usersWithAccessToNote.stream()
                .map(this::findOrCreateAppUserInDb).collect(Collectors.toList());

        appUsers.add(findOrCreateAppUserInDb(noteOwner));

        return appUsers;
    }

    private AppUser findOrCreateAppUserInDb(final KeycloakUser keycloakUser) {
        final AppUser appUser = appUserRepository.findById(keycloakUser.getId()).orElse(null);

        if (Objects.isNull(appUser)) {
            return appUserRepository.save(
                    new AppUser(keycloakUser.getId(), keycloakUser.getUsername(), new ArrayList<>()));
        } else {
            return appUser;
        }
    }
}
