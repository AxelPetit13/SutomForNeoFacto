import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GameService } from '../../services/game.service';

@Component({
  standalone: true,
  selector: 'keyboard',
  templateUrl: './keyboard.component.html',
  imports: [ReactiveFormsModule, FormsModule],
})
export class KeyboardComponent {
  protected word = '';
  @ViewChild('input') input: ElementRef<HTMLInputElement> | undefined;

  constructor(protected gameService: GameService) {
    this.word = gameService.firstLetter;
    this.gameService = gameService;
  }

  // Focus the input element just after the view is initialized
  ngAfterViewInit() {
    if (this.input !== undefined) {
      this.input.nativeElement.focus();
      this.input.nativeElement.style.opacity = '0';
      this.input.nativeElement.value = this.gameService.firstLetter;
    }
  }

  onInputChange(event: any) {
    this.gameService.currentAttempt = event.target.value;
  }

  async onEnterPress(event: any) {
    if (event.key === 'Enter') {
      const gameAnswer = await this.gameService.playGame(event.target.value);

      // If no problem, clear the input
      if (gameAnswer !== null) {
        if (this.input !== undefined) {
          this.input.nativeElement.value = this.gameService.firstLetter;
        }
      }
    }
  }
}
