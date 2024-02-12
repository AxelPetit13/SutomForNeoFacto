export enum gameErrorStatus {
  INVALID_GAME_ID = 'Game not found',
  WORD_ALREADY_TRIED = 'Invalid attempt - word already tried',
  WORD_NOT_FOUND = 'Invalid attempt - word does not exist',
  WORD_WRONG_SIZE = 'Invalid attempt - word submitted does not have the same length as the word to guess',
}
