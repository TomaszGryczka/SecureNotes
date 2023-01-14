import {Component, OnInit} from '@angular/core';
import {Note, NotesGatewayService, NoteStatus} from "../../services/notes-gateway.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-notes',
  templateUrl: './notes.component.html',
  styleUrls: ['./notes.component.css']
})
export class NotesComponent implements OnInit {

  userNotes: Note[] = [];

  constructor(private noteGateway: NotesGatewayService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.fetchUserNotes();
  }

  getNoteLabel(note: Note): string {
    const noteStatus = note.status;

    if (noteStatus === NoteStatus.PRIVATE) {
      return "PRYWATNA";
    } else if (noteStatus === NoteStatus.ENCRYPTED) {
      return "ZASZYFROWANA";
    } else if (noteStatus === NoteStatus.PUBLIC) {
      return "PUBLICZNA";
    } else {
      return "WSPÓŁDZIELONA";
    }
  }

  fetchUserNotes() {
    this.noteGateway.getUserNotes().subscribe(notes => {
      this.userNotes = notes;
    })
  }
}
