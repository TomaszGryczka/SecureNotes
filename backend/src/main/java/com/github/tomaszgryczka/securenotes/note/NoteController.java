package com.github.tomaszgryczka.securenotes.note;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/new")
    public Note saveNote(@RequestBody final Note note) {
        return noteService.saveNote(note);
    }

    @GetMapping("/{noteId}")
    public Note getNoteById(@PathVariable final Long noteId) {
        return noteService.getNoteByNoteId(noteId);
    }
}
