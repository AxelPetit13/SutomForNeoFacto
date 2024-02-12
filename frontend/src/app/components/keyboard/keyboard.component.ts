import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GameService } from '../../services/game.service';

@Component({
  standalone: true,
  selector: 'keyboard',
  templateUrl: './keyboard.component.html',
  styleUrl: './keyboard.component.css',
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

  async onEnterPress() {
    const gameAnswer = await this.gameService.playGame(this.word);

    // If no problem, clear the input
    if (gameAnswer !== null) {
      if (this.input !== undefined) {
        this.input.nativeElement.value = this.gameService.firstLetter;
      }
    }
  }

  setLetter(event: any) {
    if (this.input !== undefined) {
      if (this.input.nativeElement.value.length < this.gameService.difficulty) {
        this.input.nativeElement.value += event.currentTarget.innerText;
        this.gameService.currentAttempt = this.input.nativeElement.value;
      }
    }
  }

  deleteLastLetter() {
    if (this.input !== undefined) {
      this.input.nativeElement.value = this.input.nativeElement.value.slice(
        0,
        -1,
      );
      this.gameService.currentAttempt = this.input.nativeElement.value;
    }
  }
}
