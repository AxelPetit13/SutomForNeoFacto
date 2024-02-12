import { Component } from '@angular/core';
import { GameService } from '../../services/game.service';
import { FormsModule } from '@angular/forms';
import { NgForOf } from '@angular/common';

@Component({
  standalone: true,
  selector: 'start-form',
  templateUrl: './start-form.component.html',
  styleUrl: './start-form.component.css',
  imports: [FormsModule, NgForOf],
})
export class StartFormComponent {
  protected difficulty: string = '7';
  protected maxAttempts: string = '6';
  protected readonly Array = Array;

  constructor(private gameService: GameService) {
    this.gameService = gameService;
  }

  async startGame() {
    await this.gameService.startNewGame(
      parseInt(this.difficulty),
      parseInt(this.maxAttempts),
    );
  }
}
