package com.github.tomaszgryczka.securenotes.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.tomaszgryczka.securenotes.note.Note;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table(name = "app_users")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @Column(name = "app_user_id")
    private String id;
    private String username;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "app_user_notes",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id")
    )
    private List<Note> notes;
}
