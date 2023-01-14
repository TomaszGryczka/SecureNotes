import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Note, NotesGatewayService, NoteStatus} from "../../services/notes-gateway.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-note',
  templateUrl: './note.component.html',
  styleUrls: ['./note.component.css']
})
export class NoteComponent implements OnInit, OnDestroy {

  private routeSub?: Subscription;
  noteId = -1;
  isEncrypted = false;
  note?: Note;
  markdownText?: string;

  constructor(private noteGateway: NotesGatewayService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.routeSub = this.route.params.subscribe(params => {
      this.noteId = params['id'];
      this.routeSub = this.route.queryParams.subscribe(queryParams => {
        if(queryParams['status'] as NoteStatus === NoteStatus.ENCRYPTED) {
          this.isEncrypted = true;
        } else {
          this.noteGateway.getNoteById(this.noteId, "").subscribe(noteResp => {
            this.note = noteResp;
          })
        }
      });
    });
  }

  ngOnDestroy() {
    this.routeSub?.unsubscribe();
  }

  onSubmit(event: any) {
    const password = event.target.password.value;

    this.noteGateway.getNoteById(this.noteId, password).subscribe(
      noteResp => this.note = noteResp,
      error => window.alert("Nieprawidłowe hasło!")
      )
  }
}
