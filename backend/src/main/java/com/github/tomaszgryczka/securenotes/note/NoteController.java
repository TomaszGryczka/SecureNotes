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
    public Note saveNote(@RequestBody final NoteCreationRequest noteCreationRequest) {
        return noteService.saveNote(noteCreationRequest);
    }

    @PostMapping
    public Note getNoteById(@RequestBody final NoteRequest request) {
        return noteService.getNoteByNoteId(request.getNoteId(), request.getPassword());
    }

    @GetMapping("/user/all")
    public List<BasicNoteResponse> getUserNotes() {
        return noteService.getUserNotes();
    }
}
