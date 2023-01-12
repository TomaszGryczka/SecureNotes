import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class NotesGatewayService {

  private notesApiUrl = "http://localhost:8080/note"

  constructor(private httpClient: HttpClient) {
  }

  saveNote(markdownText: string): Observable<Note> {
    const body = {
      isEncrypted: false,
      content: markdownText
    };
    return this.httpClient.post<Note>(this.notesApiUrl + "/new", body);
  }
}

export interface Note {
  id: number,
  isEncrypted: boolean,
  content: string
}
