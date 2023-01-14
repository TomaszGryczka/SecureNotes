package com.github.tomaszgryczka.securenotes.note;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NoteRequest {
    private Long noteId;
    private String password;
}
