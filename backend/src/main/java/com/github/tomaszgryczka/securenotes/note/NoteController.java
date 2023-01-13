package com.github.tomaszgryczka.securenotes.note;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/new")
    public Note saveNote(@RequestBody final NoteRequest noteRequest) {
        return noteService.saveNote(noteRequest);
    }

    @GetMapping("/{noteId}")
    public Note getNoteById(@PathVariable final Long noteId) {
        return noteService.getNoteByNoteId(noteId);
    }

    @GetMapping("/user/all")
    public List<BasicNoteResponse> getUserNotes() {
        return noteService.getUserNotes();
    }
}
