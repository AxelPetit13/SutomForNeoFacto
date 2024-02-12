import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GameService } from '../../services/game.service';
import { NgClass } from '@angular/common';

@Component({
  standalone: true,
  selector: 'keyboard',
  templateUrl: './keyboard.component.html',
  styleUrl: './keyboard.component.css',
  imports: [ReactiveFormsModule, FormsModule, NgClass],
})
export class KeyboardComponent {
  protected word = '';
  protected colors: { [key: string]: string } = {};
  @ViewChild('input') input: ElementRef<HTMLInputElement> | undefined;

  constructor(protected gameService: GameService) {
    this.word = gameService.firstLetter;
    this.gameService = gameService;
    for (let i = 97; i <= 122; i++) {
      const letter = String.fromCharCode(i); // Convertir le code ASCII en lettre
      this.colors[letter] = 'black'; // Définir la valeur de la lettre sur 'black'
    }
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
      this.findCorrectLetters();
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

  private findCorrectLetters() {
    const attempts = this.gameService.attempts;
    const attemptsAnswers = this.gameService.attemptsAnswers;

    const letters = [];
    for (let i = 0; i < attempts.length; i++) {
      for (let j = 0; j < attempts[i].length; j++) {
        if (attemptsAnswers[i][j] !== 'INCORRECT') {
          this.colors[`${attempts[i][j]}`] = '*';
        } else {
          if (this.colors[`${attempts[i][j]}`] !== '*') {
            this.colors[`${attempts[i][j]}`] = 'grey';
          }
        }
      }
    }
  }
}
