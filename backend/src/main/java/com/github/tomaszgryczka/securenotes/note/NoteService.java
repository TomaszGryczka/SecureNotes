package com.github.tomaszgryczka.securenotes.note;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public Note saveNote(final Note note) {
        if(Objects.nonNull(note.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide note without id");
        }

        return noteRepository.save(note);
    }

    public Note getNoteByNoteId(final Long noteId) {
        return noteRepository.findById(noteId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found"));
    }
}
