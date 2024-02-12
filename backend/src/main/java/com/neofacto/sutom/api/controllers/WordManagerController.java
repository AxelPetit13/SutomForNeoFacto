package com.neofacto.sutom.api.controllers;

import com.neofacto.sutom.services.IWordManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sutom/word-manager")
public class WordManagerController {

    private final IWordManagerService service;

    @Autowired
    public WordManagerController(IWordManagerService service) {
        this.service = service;
    }

    @GetMapping("/random/{wordLength}")
    public ResponseEntity<String> getRandomWord(@PathVariable Integer wordLength) {
        String word = service.getRandomWord(wordLength);
        return ResponseEntity.ok(word);
    }

    @GetMapping("/exists/{word}")
    public ResponseEntity<Boolean> wordExists(@PathVariable String word) {
        return ResponseEntity.ok(service.wordExists(word));
    }

}
