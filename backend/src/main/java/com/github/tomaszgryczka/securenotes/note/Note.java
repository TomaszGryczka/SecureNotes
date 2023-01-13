package com.github.tomaszgryczka.securenotes.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.tomaszgryczka.securenotes.user.AppUser;
import lombok.*;

import javax.persistence.*;
import java.util.List;


@Table(name = "notes")
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @Column(name = "note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private NoteStatus noteStatus;

    @Column(length = 65535, columnDefinition="TEXT")
    private String content;

    @JsonIgnore
    @ManyToMany(mappedBy = "notes")
    private List<AppUser> users;
}
