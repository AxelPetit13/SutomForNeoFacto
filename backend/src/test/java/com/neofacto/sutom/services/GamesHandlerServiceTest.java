package com.neofacto.sutom.services;

import com.neofacto.sutom.models.Game;
import com.neofacto.sutom.models.SutomLetterStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class GamesHandlerServiceTest {

    @Test
    void testNewGame() {
        // Mock IWordManagerService
        IWordManagerService wordManagerService = Mockito.mock(IWordManagerService.class);
        when(wordManagerService.getRandomWord(5)).thenReturn("apple");

        // Create GamesHandlerService with mocked dependencies
        List<Game> games = new ArrayList<>();
        GamesHandlerService gamesHandlerService = new GamesHandlerService(games, wordManagerService);

        // Call newGame method
        Game game = gamesHandlerService.newGame(5, 3);

        // Verify that the game is added to the list and has the correct word, difficulty and maxAttempts
        assertEquals(1, games.size());
        assertEquals("apple", game.getWord());
        assertEquals(3, game.getMaxAttempts());
        assertEquals(5, game.getDifficulty());
    }

    @Test
    void testPlayGame_BadId() {
        // Mocks
        IWordManagerService wordManagerService = Mockito.mock(IWordManagerService.class);
        when(wordManagerService.wordExists("test")).thenReturn(true);
        when(wordManagerService.getRandomWord(4)).thenReturn("test");

        Game game = new Game("test", 3);

        List<Game> games = new ArrayList<>();
        games.add(game);

        // Create service instance
        GamesHandlerService gamesHandlerService = new GamesHandlerService(games, wordManagerService);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            gamesHandlerService.playGame("nonexistent_id", "test");
        });
    }

    @Test
    void testPlayGame_WordAlreadyTried() {
        // Mocks
        IWordManagerService wordManagerService = Mockito.mock(IWordManagerService.class);
        when(wordManagerService.wordExists("test")).thenReturn(true);
        when(wordManagerService.wordExists("wxyz")).thenReturn(true);
        when(wordManagerService.getRandomWord(4)).thenReturn("test");

        Game game = new Game("test", 3);


        List<Game> games = new ArrayList<>();
        games.add(game);

        // Create service instance
        GamesHandlerService gamesHandlerService = new GamesHandlerService(games, wordManagerService);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            gamesHandlerService.playGame(game.getId(), "wxyz");
            gamesHandlerService.playGame(game.getId(), "wxyz");
        });
    }

    @Test
    void testPlayGame_WordLengthMismatch() {
        // Mocks
        IWordManagerService wordManagerService = Mockito.mock(IWordManagerService.class);
        when(wordManagerService.wordExists("test")).thenReturn(true);
        when(wordManagerService.wordExists("abcde")).thenReturn(true);
        when(wordManagerService.getRandomWord(4)).thenReturn("test");

        Game game = new Game("test", 3);

        List<Game> games = new ArrayList<>();
        games.add(game);

        // Create service instance
        GamesHandlerService gamesHandlerService = new GamesHandlerService(games, wordManagerService);

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            gamesHandlerService.playGame(game.getId(), "abcde");
        });
    }

    @Test
    void testPlayGame_GameWon() {
        // Mocks
        IWordManagerService wordManagerService = Mockito.mock(IWordManagerService.class);
        when(wordManagerService.wordExists("test")).thenReturn(true);
        when(wordManagerService.getRandomWord(4)).thenReturn("test");

        Game game = new Game("test", 3);

        List<Game> games = new ArrayList<>();
        games.add(game);

        // Create service instance
        GamesHandlerService gamesHandlerService = new GamesHandlerService(games, wordManagerService);

        // Assert
        List<SutomLetterStatus> result = gamesHandlerService.playGame(game.getId(), "test");
        assertEquals(4, result.size());
        assertEquals(SutomLetterStatus.CORRECT, result.get(0));
        assertEquals(SutomLetterStatus.CORRECT, result.get(1));
        assertEquals(SutomLetterStatus.CORRECT, result.get(2));
        assertTrue(games.isEmpty()); // Game should be removed from the list
    }

    @Test
    void testPlayGame_GameOver() {
        // Mocks
        IWordManagerService wordManagerService = Mockito.mock(IWordManagerService.class);
        when(wordManagerService.wordExists("test")).thenReturn(true);
        when(wordManagerService.wordExists("abcd")).thenReturn(true);
        when(wordManagerService.wordExists("efgh")).thenReturn(true);
        when(wordManagerService.wordExists("ijkl")).thenReturn(true);
        when(wordManagerService.wordExists("mnop")).thenReturn(true);
        when(wordManagerService.getRandomWord(4)).thenReturn("test");

        Game game = new Game("test", 3);

        List<Game> games = new ArrayList<>();
        games.add(game);

        // Create service instance
        GamesHandlerService gamesHandlerService = new GamesHandlerService(games, wordManagerService);

        // Play
        gamesHandlerService.playGame(game.getId(), "abcd");
        gamesHandlerService.playGame(game.getId(), "efgh");
        gamesHandlerService.playGame(game.getId(), "ijkl");

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            gamesHandlerService.playGame(game.getId(), "mnop");
        });
        assertTrue(games.isEmpty()); // Game should be removed from the list
    }


    @Test
    void testTranslateIntoSutomWord_sameLengthCorrectWord() {
        GamesHandlerService gamesHandlerService = new GamesHandlerService(null, null);

        String word = "apple";
        String attempt = "apple";

        List<SutomLetterStatus> result = gamesHandlerService.translateIntoSutomWord(word, attempt);

        assertEquals(5, result.size());
        assertTrue(result.stream().allMatch(status -> status == SutomLetterStatus.CORRECT));
    }

    @Test
    void testTranslateIntoSutomWord_sameLengthIncorrectWord() {
        GamesHandlerService gamesHandlerService = new GamesHandlerService(null, null);

        String word = "apple";
        String attempt = "peach";

        List<SutomLetterStatus> result = gamesHandlerService.translateIntoSutomWord(word, attempt);

        assertEquals(5, result.size());
        assertTrue(result.stream().allMatch(status -> status != SutomLetterStatus.CORRECT));
    }

    @Test
    void testTranslateIntoSutomWord_misplacedLetters_1() {
        GamesHandlerService gamesHandlerService = new GamesHandlerService(null, null);

        String word = "apple";
        String attempt = "ample";

        List<SutomLetterStatus> result = gamesHandlerService.translateIntoSutomWord(word, attempt);

        assertEquals(5, result.size());
        assertEquals(SutomLetterStatus.CORRECT, result.get(0));
        assertEquals(SutomLetterStatus.INCORRECT, result.get(1));
        assertEquals(SutomLetterStatus.CORRECT, result.get(2));
        assertEquals(SutomLetterStatus.CORRECT, result.get(3));
        assertEquals(SutomLetterStatus.CORRECT, result.get(4));
    }

    @Test
    void testTranslateIntoSutomWord_misplacedLetters_2() {
        GamesHandlerService gamesHandlerService = new GamesHandlerService(null, null);

        String word = "tailors";
        String attempt = "billion";

        List<SutomLetterStatus> result = gamesHandlerService.translateIntoSutomWord(word, attempt);

        assertEquals(7, result.size());
        assertEquals(SutomLetterStatus.INCORRECT, result.get(0));
        assertEquals(SutomLetterStatus.MISPLACED, result.get(1));
        assertEquals(SutomLetterStatus.INCORRECT, result.get(2));
        assertEquals(SutomLetterStatus.CORRECT, result.get(3));
        assertEquals(SutomLetterStatus.INCORRECT, result.get(4));
        assertEquals(SutomLetterStatus.MISPLACED, result.get(5));
        assertEquals(SutomLetterStatus.INCORRECT, result.get(6));
    }

    @Test
    void testTranslateIntoSutomWord_differentLength() {
        GamesHandlerService gamesHandlerService = new GamesHandlerService(null, null);

        String word = "apple";
        String attempt = "app";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gamesHandlerService.translateIntoSutomWord(word, attempt);
        });

        assertEquals("The two words do not have the same length", exception.getMessage());
    }





}
