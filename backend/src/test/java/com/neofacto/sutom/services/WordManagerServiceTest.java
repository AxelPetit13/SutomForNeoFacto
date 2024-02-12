package com.neofacto.sutom.services;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WordManagerServiceTest {

    @Test
    void testGetRandomWord_WordLenghtExists() {
        // Mock wordMap
        Map<Integer, List<String>> wordMap = new HashMap<>();
        wordMap.put(4, Arrays.asList("pear", "plum", "kiwi"));
        wordMap.put(5, Arrays.asList("apple", "peach"));

        WordManagerService wordManagerService = new WordManagerService(wordMap);

        String word1 = wordManagerService.getRandomWord(5);
        assertNotNull(word1);
        assertEquals(5, word1.length());
        assertTrue(wordMap.get(5).contains(word1));
    }

    @Test
    void testGetRandomWord_WordLenghtDoesntExist() {
        // Mock wordMap
        Map<Integer, List<String>> wordMap = new HashMap<>();
        wordMap.put(4, Arrays.asList("pear", "plum", "kiwi"));
        wordMap.put(5, Arrays.asList("apple", "peach"));

        WordManagerService wordManagerService = new WordManagerService(wordMap);

        String word2 = wordManagerService.getRandomWord(6);
        assertNull(word2);
        assertFalse(wordMap.get(5).contains("random_word"));
    }


    @Test
    void testWordExists() {
        // Mock wordMap
        Map<Integer, List<String>> wordMap = new HashMap<>();
        wordMap.put(5, Arrays.asList("apple", "banana", "peach"));
        wordMap.put(4, Arrays.asList("pear", "plum", "kiwi"));

        WordManagerService wordManagerService = new WordManagerService(wordMap);

        assertTrue(wordManagerService.wordExists("apple"));
        assertFalse(wordManagerService.wordExists("orange"));
        assertFalse(wordManagerService.wordExists("grape"));
    }
}