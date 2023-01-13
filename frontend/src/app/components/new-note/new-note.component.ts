import {Component, OnInit} from '@angular/core';
import {EditorInstance, EditorOption} from "angular-markdown-editor";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MarkdownService} from "ngx-markdown";
import {Note, NotesGatewayService, NoteStatus} from "../../services/notes-gateway.service";
import {User, UsersGatewayService} from "../../services/users-gateway.service";

@Component({
  selector: 'app-new-note',
  templateUrl: './new-note.component.html',
  styleUrls: ['./new-note.component.css']
})
export class NewNoteComponent implements OnInit {

  bsEditorInstance!: EditorInstance;
  markdownText = '';
  templateForm!: FormGroup;
  noteForm!: FormGroup;
  editorOptions!: EditorOption;
  users: User[] = [];
  selectedUsersToShareNote: User[] = [];

  passwordRegex = "^(?=.*[A-Z].*[A-Z])(?=.*[!@#$&*])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{8,}$"

  constructor(private fb: FormBuilder,
              private markdownService: MarkdownService,
              private notesGateway: NotesGatewayService,
              private usersGateway: UsersGatewayService) {
  }

  ngOnInit(): void {
    this.editorOptions = {
      autofocus: false,
      iconlibrary: 'fa',
      savable: false,
      onShow: (e) => this.bsEditorInstance = e,
      parser: (val) => this.parse(val),
    };

    // default text
    this.markdownText =
      `**Pogrubione słowo**

_Pochylone słowo_

[Odnośnik do zewnętrzengo serwisu](https://100procent-natury.pl/userdata/public/gfx/852/mandary.jpg)

# Jeden
## Dwa
### Trzy
#### Cztery
##### Pięć

![MANDARYNKI](https://100procent-natury.pl/userdata/public/gfx/852/mandary.jpg "mandarynki :)")`;
    this.buildForm(this.markdownText);
    this.buildNoteForm();
    this.onFormChanges();
    this.fetchUsers();
  }

  fetchUsers() {
    this.usersGateway.getAllUsers().subscribe(users => {
      this.users = users;
    })
  }

  buildForm(markdownText: string) {
    this.templateForm = this.fb.group({
      body: [markdownText],
      isPreview: [true]
    });
  }

  buildNoteForm() {
    this.noteForm = this.fb.group({
      privateNote: new FormControl(false, []),
      publicNote: new FormControl(false, []),
      sharedNote: new FormControl(false, []),
      encryptedNote: new FormControl(false, []),
      password: new FormControl(false, [Validators.required, Validators.pattern(this.passwordRegex)])
    });
  }

  get isPublicNoteCheckBoxDisabled() {
    return this.noteForm.get("privateNote")?.value || this.noteForm.get("encryptedNote")?.value
      || this.noteForm.get("sharedNote")?.value;
  }

  get isPrivateNoteCheckBoxDisabled() {
    return this.noteForm.get("encryptedNote")?.value || this.noteForm.get("publicNote")?.value
      || this.noteForm.get("sharedNote")?.value;
  }

  get isSharedNoteCheckBoxDisabled() {
    return this.noteForm.get("privateNote")?.value || this.noteForm.get("publicNote")?.value
      || this.noteForm.get("encryptedNote")?.value;
  }

  get isEncryptedNoteCheckBoxDisabled() {
    return this.noteForm.get("privateNote")?.value || this.noteForm.get("publicNote")?.value
      || this.noteForm.get("sharedNote")?.value;
  }

  get isSharedNoteSelected() {
    return this.noteForm.get("sharedNote")?.value;
  }

  get isEncryptedNoteSelected() {
    return this.noteForm.get("encryptedNote")?.value;
  }

  get isNoteButtonAvailable() {
    return (this.noteForm.get("encryptedNote")?.value && this.noteForm.get("password")?.valid)
      || this.noteForm.get("sharedNote")?.value || this.noteForm.get("publicNote")?.value
      || this.noteForm.get("privateNote")?.value;
  }

  onChange(user: User, isChecked: boolean) {
    if (isChecked) {
      this.selectedUsersToShareNote.push(user);
    } else {
      let index = this.selectedUsersToShareNote.indexOf(user);
      this.selectedUsersToShareNote.splice(index, 1);
    }
  }

  highlight() {
    setTimeout(() => {
      this.markdownService.highlight();
    });
  }

  parse(inputValue: string) {
    const markedOutput = this.markdownService.parse(inputValue.trim());
    this.highlight();

    return markedOutput;
  }

  onFormChanges(): void {
    this.templateForm.valueChanges.subscribe(formData => {
      if (formData) {
        this.markdownText = formData.body;
      }
    });
  }

  saveNote() {
    const noteData = {
      status: this.noteStatus,
      password: this.noteForm.get("password")?.value,
      content: this.markdownText,
      sharedToUsers: this.selectedUsersToShareNote
    } as Note;

    this.notesGateway.saveNote(noteData).subscribe(response => {
      window.alert("Notatka zapisana!");
    });
  }

  get noteStatus(): NoteStatus {
    if(this.noteForm.get("encryptedNote")?.value) {
      return NoteStatus.ENCRYPTED;
    } else if(this.noteForm.get("publicNote")?.value) {
      return NoteStatus.PUBLIC;
    } else if(this.noteForm.get("privateNote")?.value) {
      return NoteStatus.PRIVATE;
    } else {
      return NoteStatus.SHARED;
    }
  }
}
