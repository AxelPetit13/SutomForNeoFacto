import { EventEmitter, Injectable } from '@angular/core';
import { BackendService } from './backend.service';
import { gameErrorStatus } from '../enums/game-error.enum';

// noinspection JSUnusedGlobalSymbols
@Injectable({
  providedIn: 'root',
})
export class GameService {
  private _gameId = '';
  private _gameStarted = false;
  private _difficulty = 0;
  private _maxAttempts = 0;
  private _firstLetter = '';
  private _currentAttemptChanged: EventEmitter<string> =
    new EventEmitter<string>();
  private _attempts: string[] = [];
  private _attemptsAnswers: string[] = [];
  private _attemptsAnswersChanged: EventEmitter<string[]> = new EventEmitter<
    string[]
  >();
  private _gameIsWon: boolean | undefined = undefined;
  private _errorStatus?: gameErrorStatus;

  constructor(private _backendService: BackendService) {
    this._backendService = _backendService;
  }

  get gameStarted(): boolean {
    return this._gameStarted;
  }

  get gameIsWon(): boolean | undefined {
    return this._gameIsWon;
  }

  get maxAttempts() {
    return this._maxAttempts;
  }

  get difficulty() {
    return this._difficulty;
  }

  get firstLetter() {
    return this._firstLetter;
  }

  set currentAttempt(currentAttempt: string) {
    this._currentAttemptChanged.emit(currentAttempt);
  }

  get attempts() {
    return this._attempts;
  }

  get currentAttemptChanged() {
    return this._currentAttemptChanged;
  }

  get attemptsAnswers() {
    return this._attemptsAnswers;
  }

  get attemptsAnswersChanged() {
    return this._attemptsAnswersChanged;
  }

  get errorStatus() {
    return this._errorStatus;
  }

  async startNewGame(difficulty: number, maxAttempts: number) {
    const newGame = await this._backendService.startNewGame(
      difficulty,
      maxAttempts,
    );
    if (newGame === null) {
      console.error('Failed to start a new components');
      return;
    }
    this._gameId = newGame.id;
    this._firstLetter = newGame.firstLetter;
    this._gameStarted = true;
    this._difficulty = difficulty;
    this._maxAttempts = maxAttempts;
  }

  async playGame(guess: string) {
    const gameAnswer = await this._backendService.playGame(this._gameId, guess);
    if (gameAnswer === null) {
      console.error('Failed to play');
      return null;
    }

    switch (gameAnswer) {
      case gameErrorStatus.INVALID_GAME_ID:
        this._errorStatus = gameErrorStatus.INVALID_GAME_ID;
        return null;
      case gameErrorStatus.WORD_ALREADY_TRIED:
        this._errorStatus = gameErrorStatus.WORD_ALREADY_TRIED;
        return null;
      case gameErrorStatus.WORD_NOT_FOUND:
        this._errorStatus = gameErrorStatus.WORD_NOT_FOUND;
        return null;
      case gameErrorStatus.WORD_WRONG_SIZE:
        this._errorStatus = gameErrorStatus.WORD_WRONG_SIZE;
        return null;
      default:
        this._errorStatus = undefined;
        this._attempts.push(guess);
        this._attemptsAnswers.push(gameAnswer);
        this._attemptsAnswersChanged.emit(this._attemptsAnswers.slice());

        // Check if the game is won
        const correctAnswers = gameAnswer.filter(
          (answer: string) => answer === 'CORRECT',
        );
        if (correctAnswers.length === this._difficulty) {
          this._gameIsWon = true;
        }
        if (this._attempts.length === this._maxAttempts) {
          this._gameIsWon = false;
        }
        return gameAnswer;
    }
  }

  resetGame() {
    this._gameId = '';
    this._gameStarted = false;
    this._difficulty = 0;
    this._maxAttempts = 0;
    this._firstLetter = '';
    this._currentAttemptChanged = new EventEmitter<string>();
    this._attempts = [];
    this._attemptsAnswers = [];
    this._attemptsAnswersChanged = new EventEmitter<string[]>();
    this._gameIsWon = undefined;
  }
}
