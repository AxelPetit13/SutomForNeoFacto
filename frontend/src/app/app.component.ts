import { Component, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { GridComponent } from './components/grid/grid.component';
import {
  NgClass,
  NgForOf,
  NgIf,
  NgSwitch,
  NgSwitchCase,
} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GameService } from './services/game.service';
import { StartFormComponent } from './components/start-form/start-form.component';
import { KeyboardComponent } from './components/keyboard/keyboard.component';
import { BouncingComponent } from './components/boucing-container/bouncing.component';
import { CursorComponent } from './components/cursor/cursor.component';
import { gameErrorStatus } from './enums/game-error.enum';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    GridComponent,
    NgIf,
    FormsModule,
    NgForOf,
    StartFormComponent,
    KeyboardComponent,
    NgClass,
    BouncingComponent,
    CursorComponent,
    NgSwitchCase,
    NgSwitch,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  @ViewChild(KeyboardComponent) keyboard: KeyboardComponent | undefined;
  constructor(protected gameService: GameService) {
    this.gameService = gameService;
  }

  keepFocusOnInput() {
    if (this.keyboard !== undefined) {
      this.keyboard.input?.nativeElement.focus();
    }
  }

  replay() {
    this.gameService.resetGame();
  }

  protected readonly gameErrorStatus = gameErrorStatus;
}
