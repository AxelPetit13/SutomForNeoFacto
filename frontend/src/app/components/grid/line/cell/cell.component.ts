import { Component, ElementRef, Input } from '@angular/core';
import { GameService } from '../../../../services/game.service';
import { NgClass } from '@angular/common';

import { BouncingComponent } from '../../../boucing-container/bouncing.component';

@Component({
  selector: 'cell',
  templateUrl: './cell.component.html',
  styleUrl: './cell.component.css',
  standalone: true,
  imports: [NgClass, BouncingComponent, BouncingComponent],
})
export class CellComponent {
  @Input() lineIndex = 0;
  @Input() columnIndex = 0;
  protected color = 'black';
  protected letter = '';

  constructor(
    private gameService: GameService,
    private elementRef: ElementRef,
  ) {
    this.gameService = gameService;
  }

  ngOnInit() {
    // Fill the current line with .
    if (this.lineIndex === 0) {
      this.letter = '.';
      if (this.columnIndex === 0) {
        this.letter = this.gameService.firstLetter;
      }
    }

    // Detect the keyboard input
    this.gameService.currentAttemptChanged.subscribe((currentAttempt) => {
      // If the cell is on the current line :
      if (this.lineIndex === this.gameService.attempts.length) {
        this.letter = '.';
        // If the current attempt length is long enough
        if (this.columnIndex <= currentAttempt.length - 1) {
          this.letter = currentAttempt[this.columnIndex];
        } else if (this.columnIndex > currentAttempt.length - 1) {
          this.letter = '.';
        }
      }
    });

    // Set the colors and the correct letters
    this.gameService.attemptsAnswersChanged.subscribe((answers: string[]) => {
      // color the cells
      if (this.lineIndex === this.gameService.attempts.length - 1) {
        const answer =
          this.gameService.attemptsAnswers[this.lineIndex][this.columnIndex];
        switch (answer) {
          case 'CORRECT':
            this.color = 'red';
            break;
          case 'MISPLACED':
            this.color = 'orange';
            break;
          default:
            this.color = 'blue';
        }
      }
      // Fill the next line with dots
      if (this.lineIndex === this.gameService.attempts.length) {
        this.letter = '.';
        if (this.columnIndex === 0) {
          this.letter = this.gameService.firstLetter;
        }
        const answer =
          this.gameService.attemptsAnswers[this.lineIndex - 1][
            this.columnIndex
          ];
        if (answer === 'CORRECT') {
          this.letter =
            this.gameService.attempts[this.lineIndex - 1][this.columnIndex];
        }
      }
    });
  }
}
