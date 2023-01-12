import {Component, OnInit} from '@angular/core';
import {EditorInstance, EditorOption} from "angular-markdown-editor";
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {MarkdownService} from "ngx-markdown";
import {NotesGatewayService} from "../../services/notes-gateway.service";

@Component({
  selector: 'app-new-note',
  templateUrl: './new-note.component.html',
  styleUrls: ['./new-note.component.css']
})
export class NewNoteComponent implements OnInit {

  bsEditorInstance!: EditorInstance;
  markdownText = '';
  showEditor = true;
  templateForm!: FormGroup;
  noteForm!: FormGroup;
  editorOptions!: EditorOption;

  constructor(private fb: FormBuilder,
              private markdownService: MarkdownService,
              private notesGateway: NotesGatewayService) {
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
      sharedNote: new FormControl(false, [])
    });
  }

  get isPublicNoteCheckBoxDisabled() {
    return this.noteForm.get("privateNote")?.value || this.noteForm.get("sharedNote")?.value;
  }

  get isPrivateNoteCheckBoxDisabled() {
    return this.noteForm.get("publicNote")?.value || this.noteForm.get("sharedNote")?.value;
  }

  get isSharedNoteCheckBoxDisabled() {
    return this.noteForm.get("privateNote")?.value || this.noteForm.get("publicNote")?.value;
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
      console.log(formData)
      if (formData) {
        this.markdownText = formData.body;
      }
    });
  }

  saveNote() {
    this.notesGateway.saveNote(this.markdownText).subscribe(response => {
      window.alert("Notatka zapisana!");
    });
  }
}
