package com.github.tomaszgryczka.securenotes.note;

import lombok.*;

import javax.persistence.*;


@Table
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isEncrypted;

    @Column(length = 65535, columnDefinition="TEXT")
    private String content;
}
