package com.neofacto.sutom.services;

import com.neofacto.sutom.models.Game;
import com.neofacto.sutom.models.SutomLetterStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GamesHandlerService implements IGamesHandlerService {
    private final List<Game> games;

    private final IWordManagerService wordManagerService;

    public GamesHandlerService(List<Game> games, IWordManagerService wordManagerService) {
        this.games = games;
        this.wordManagerService = wordManagerService;
    }

    public List<Game> getAllGames() {
        return games;
    }

    public Optional<Game> getGameById(String id) {
        return games.stream().filter(game -> game.getId().equals(id)).findFirst();
    }

    private void addGame(Game game) {
        games.add(game);
    }

    private void removeGame(Game game) {
        games.remove(game);
    }

    public Game newGame(Integer difficulty, Integer maxAttempts) {
        String word = wordManagerService.getRandomWord(difficulty);
        Game game = new Game(word, maxAttempts);
        System.out.println("New game created with id : " + game.getId() + " and word : " + word);
        addGame(game);
        return game;
    }

    public List<SutomLetterStatus> playGame(String id, String attempt) {
        Optional<Game> gameExists = getGameById(id);
        if (gameExists.isEmpty()) {
            throw new IllegalArgumentException("Game not found");
        }

        Game game = gameExists.get();

        // Check if the attempt is valid
        if (game.getAttempts().contains(attempt)) {
            throw new IllegalArgumentException("Invalid attempt - word already tried");
        }

        if (attempt.length() != game.getDifficulty()) {
            throw new IllegalArgumentException("Invalid attempt - word submitted does not have the same length as the word to guess");
        }

        if (!wordManagerService.wordExists(attempt)) {
            throw new IllegalArgumentException("Invalid attempt - word does not exist");
        }

        // Add attempt to game
        game.addAttempt(attempt);

        // Check if game is finished
        if (game.getWord().equals(attempt) || game.getAttempts().size() >= game.getMaxAttempts()) {
            removeGame(game);
        }
        return translateIntoSutomWord(game.getWord(), attempt);
    }


    public List<SutomLetterStatus> translateIntoSutomWord(String word, String attempt) {
        // Check if the two words have the same length
        if (word.length() != attempt.length()) {
            throw new IllegalArgumentException("The two words do not have the same length");
        }

        // Count occurrences of each letter in word
        int[] wordLetterCounts = new int[26]; // 26 letters in the alphabet (a-z), the game is in English so no accents or special characters are expected.
        for (char c : word.toCharArray()) {
            wordLetterCounts[c - 'a']++;
        }

        // Count occurrences of each letter in attempt
        int[] attemptLetterCounts = new int[26];
        for (char c : attempt.toCharArray()) {
            attemptLetterCounts[c - 'a']++;
        }

        // Create a list of SutomLetterStatus with the length of the word
        List<SutomLetterStatus> letterStatuses = new ArrayList<>(Collections.nCopies(word.length(), SutomLetterStatus.INCORRECT));

        // Iterate through each letter in attempt to find the correct letters
        for (int i = 0; i < attempt.length(); i++) {
            char letter = attempt.charAt(i);
            if (word.charAt(i) == letter) {
                letterStatuses.set(i, SutomLetterStatus.CORRECT);
                wordLetterCounts[letter - 'a']--;
                attemptLetterCounts[letter - 'a']--;
            }
        }
        // Then, find the misplaced letters
        for (int i = 0; i < attempt.length(); i++) {
            char letter = attempt.charAt(i);
            if (wordLetterCounts[letter - 'a'] > 0 && attemptLetterCounts[letter - 'a'] > 0 ) {
                letterStatuses.set(i, SutomLetterStatus.MISPLACED);
                wordLetterCounts[letter - 'a']--;
                attemptLetterCounts[letter - 'a']--;
            }

        }


        return letterStatuses;
    }
}
