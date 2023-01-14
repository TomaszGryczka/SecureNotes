package com.github.tomaszgryczka.securenotes.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT note FROM Note note JOIN note.users users WHERE users.id = ?1 OR note.noteStatus = 1")
    List<Note> getUsersNotes(String userId);
}
