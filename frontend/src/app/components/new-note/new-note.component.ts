import {Component, OnInit} from '@angular/core';
import {EditorInstance, EditorOption} from "angular-markdown-editor";
import {FormBuilder, FormGroup} from "@angular/forms";
import {MarkdownService} from "ngx-markdown";

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
  editorOptions!: EditorOption;

  constructor(private fb: FormBuilder,
              private markdownService: MarkdownService) {
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

![enter image description here](https://100procent-natury.pl/userdata/public/gfx/852/mandary.jpg "enter image title here")`;
    this.buildForm(this.markdownText);
    this.onFormChanges();
  }

  buildForm(markdownText: string) {
    this.templateForm = this.fb.group({
      body: [markdownText],
      isPreview: [true]
    });
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
}
