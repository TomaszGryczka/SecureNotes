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
import {AngularMarkdownEditorModule} from "angular-markdown-editor";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MarkdownModule, MarkedOptions} from "ngx-markdown";

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
    KeycloakAngularModule,
    AngularMarkdownEditorModule.forRoot({
      iconlibrary: 'fa'
    }),
    MarkdownModule.forRoot({
      markedOptions: {
        provide: MarkedOptions,
        useValue: {
          gfm: true,
          breaks: false,
          pedantic: false,
          smartLists: true,
          smartypants: false,
        },
      },
    }),
    FormsModule,
    MarkdownModule,
    ReactiveFormsModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService],
    },
    AngularMarkdownEditorModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
