import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NotesComponent } from './components/notes/notes.component';
import { MainComponent } from './components/main/main.component';
import { NewNoteComponent } from './components/new-note/new-note.component';
import { NoteComponent } from './components/note/note.component';
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import {initializeKeycloak} from "./init/keycloak-init.factory";

@NgModule({
  declarations: [
    AppComponent,
    NotesComponent,
    MainComponent,
    NewNoteComponent,
    NoteComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    KeycloakAngularModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService],
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
