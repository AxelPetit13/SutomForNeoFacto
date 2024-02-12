package com.neofacto.sutom.api.controllers;

import com.neofacto.sutom.models.Game;
import com.neofacto.sutom.models.SutomLetterStatus;
import com.neofacto.sutom.services.IGamesHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;

@RestController
@RequestMapping("/api/v1/sutom/games")
@CrossOrigin(origins = "*")
public class GamesHandlerController {

    private final IGamesHandlerService service;

    @Autowired
    public GamesHandlerController(IGamesHandlerService service) {
        this.service = service;
    }

    @GetMapping("/new/{difficulty}/{maxAttempts}")
    public ResponseEntity<Map<String, Object>> newGame(@PathVariable Integer difficulty , @PathVariable Integer maxAttempts) {
        Game game = service.newGame(difficulty, maxAttempts);

        // Create an object for the response
        Map<String, Object> response = new HashMap<>();
        response.put("id", game.getId());
        response.put("firstLetter", game.getWord().charAt(0));

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = service.getAllGames();
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        Optional<Game> game = service.getGameById(id);
        if (game.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(game.get());
    }

    @PostMapping("/{id}/play/{attempt}")
    public ResponseEntity playGame(@PathVariable String id, @PathVariable String attempt) {
        try {
            List<SutomLetterStatus> sutomWord = service.playGame(id, attempt);
            return ResponseEntity.ok(sutomWord);
        }
        catch (Exception exception) {
            System.out.println(exception);
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
