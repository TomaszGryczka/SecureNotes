package com.github.tomaszgryczka.securenotes.note;

import com.github.tomaszgryczka.securenotes.keycloak.KeycloakUser;
import com.github.tomaszgryczka.securenotes.keycloak.KeycloakUserService;
import com.github.tomaszgryczka.securenotes.user.AppUser;
import com.github.tomaszgryczka.securenotes.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
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

    @Value("${app.salt}")
    private String salt;

    public Note saveNote(final NoteCreationRequest noteCreationRequest) {
        final KeycloakUser noteOwner = keycloakUserService.getKeycloakUserInfoFromJwt();
        final List<AppUser> usersWithAccessToNote =
                getUsersThatWillHaveAccessToNote(noteOwner, noteCreationRequest.getSharedToUsers());

        final String content = decryptContentFromRequestIfNeeded(noteCreationRequest);

        final Note noteToAdd = Note.builder()
                .noteStatus(noteCreationRequest.getStatus())
                .content(content)
                .users(usersWithAccessToNote)
                .build();

        usersWithAccessToNote.forEach(user -> user.getNotes().add(noteToAdd));

        return noteRepository.save(noteToAdd);
    }

    private String decryptContentFromRequestIfNeeded(final NoteCreationRequest noteCreationRequest) {
        if (noteCreationRequest.getStatus() == NoteStatus.ENCRYPTED) {
            final String password = noteCreationRequest.getPassword();
            final TextEncryptor textEncryptor = Encryptors.text(password, salt);

            return textEncryptor.encrypt(noteCreationRequest.getContent());
        } else {
            return noteCreationRequest.getContent();
        }
    }

    public Note getNoteByNoteId(final Long noteId, final String password) {
        final KeycloakUser user = keycloakUserService.getKeycloakUserInfoFromJwt();

        final Note note = noteRepository.getUsersNotes(user.getId()).stream()
                .filter(it -> it.getId().equals(noteId))
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));

        if (note.getNoteStatus() == NoteStatus.ENCRYPTED) {
            final TextEncryptor textEncryptor = Encryptors.text(password, salt);

            try {
                final String decryptedContent = textEncryptor.decrypt(note.getContent());
                note.setContent(decryptedContent);
            } catch (Exception ex) {
                log.info("Bad password used for decryption of note content...");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bad password");
            }
        }

        return note;
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
