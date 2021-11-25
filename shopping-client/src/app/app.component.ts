import { Component } from '@angular/core';
import * as uuid from 'uuid';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'shopping-client';
  ready: boolean;

  constructor() {
    let sessionId = uuid.v4();
    localStorage.setItem('sessionId', sessionId);
    this.ready = true;
  }
}
