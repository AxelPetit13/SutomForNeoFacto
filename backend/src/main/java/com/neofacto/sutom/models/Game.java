package com.neofacto.sutom.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private final String id;
    private final Integer difficulty;
    private final Integer maxAttempts;
    private final String word;
    private final List<String> attempts;

    public Game(String word, Integer maxAttempts) {
        this.id = UUID.randomUUID().toString();
        this.difficulty = word.length();
        this.maxAttempts = maxAttempts;
        this.word = word;
        this.attempts = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public String getWord() {
        return word;
    }

    public List<String> getAttempts() {
        return attempts;
    }

    public void addAttempt(String attempt) {
        attempts.add(attempt);
    }

}
