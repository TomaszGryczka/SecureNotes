import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "./users-gateway.service";

@Injectable({
  providedIn: 'root'
})
export class NotesGatewayService {

  private notesApiUrl = "http://localhost:8080/note"

  constructor(private httpClient: HttpClient) {
  }

  saveNote(note: Note): Observable<Note> {
    return this.httpClient.post<Note>(this.notesApiUrl + "/new", note);
  }

  getUserNotes(): Observable<Note[]> {
    return this.httpClient.get<Note[]>(this.notesApiUrl + "/user/all");
  }

  getNoteById(noteId: number, password: string): Observable<Note> {
    const body = {
      noteId: noteId,
      password: password
    }

    return this.httpClient.post<Note>(this.notesApiUrl, body);
  }
}

export interface Note {
  id: number,
  status: NoteStatus
  password: string,
  content: string
  sharedToUsers: User[];
}

export enum NoteStatus {
  ENCRYPTED = "ENCRYPTED",
  PUBLIC = "PUBLIC",
  PRIVATE = "PRIVATE",
  SHARED = "SHARED"
}
