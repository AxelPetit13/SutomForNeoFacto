package com.neofacto.sutom.services;

import com.neofacto.sutom.models.Game;
import com.neofacto.sutom.models.SutomLetterStatus;

import java.util.List;
import java.util.Optional;


public interface IGamesHandlerService {
    List<Game> getAllGames();

    Optional<Game> getGameById(String id);

    Game newGame(Integer difficulty, Integer maxAttempts);

    List<SutomLetterStatus> playGame(String id, String attempt);
}
