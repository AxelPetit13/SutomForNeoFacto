import { Injectable } from '@angular/core';
import { gameErrorStatus } from '../enums/game-error.enum';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class BackendService {
  async startNewGame(difficulty: number, maxAttempts: number) {
    const url = environment.apiUrl + `/new/${difficulty}/${maxAttempts}`;

    try {
      const res = await fetch(url, {
        method: 'GET',
      });
      if (res.ok) {
        return await res.json();
      } else {
        console.error(res.statusText);
        return null;
      }
    } catch (error) {
      console.error('A error occurred:', error);
      return null;
    }
  }

  async playGame(gameId: string, guess: string) {
    const url = environment.apiUrl + `/${gameId}/play/${guess}`;

    try {
      const res = await fetch(url, {
        method: 'POST',
      });
      if (res.ok) {
        return await res.json();
      } else {
        if (res.status === 400) {
          const message = await res.text();
          console.error(message);
          switch (message) {
            case gameErrorStatus.INVALID_GAME_ID:
              return gameErrorStatus.INVALID_GAME_ID;
            case gameErrorStatus.WORD_ALREADY_TRIED:
              return gameErrorStatus.WORD_ALREADY_TRIED;
            case gameErrorStatus.WORD_NOT_FOUND:
              return gameErrorStatus.WORD_NOT_FOUND;
            case gameErrorStatus.WORD_WRONG_SIZE:
              return gameErrorStatus.WORD_WRONG_SIZE;
          }
        }
        return null;
      }
    } catch (error) {
      console.error('A error occurred:', error);
      return null;
    }
  }
}
