package com.github.tomaszgryczka.securenotes.note;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicNoteResponse {
    private Long id;
    private NoteStatus status;
}
