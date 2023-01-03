import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainComponent} from "./components/main/main.component";
import {AuthGuard} from "./guard/auth.guard";
import {NewNoteComponent} from "./components/new-note/new-note.component";
import {NoteComponent} from "./components/note/note.component";
import {NotesComponent} from "./components/notes/notes.component";

const routes: Routes = [
  // {path: 'main', component: MainComponent, canActivate: [AuthGuard]},
  // {path: 'new/note', component: NewNoteComponent, canActivate: [AuthGuard]},
  // {path: 'note/:id', component: NoteComponent, canActivate: [AuthGuard]},
  // {path: 'notes/:username', component: NotesComponent, canActivate: [AuthGuard]},
  // {path: '**', redirectTo: '/main'}
  {path: 'main', component: MainComponent},
  {path: 'new/note', component: NewNoteComponent},
  {path: 'note/:id', component: NoteComponent},
  {path: 'notes/:username', component: NotesComponent},
  {path: '**', redirectTo: '/main'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
