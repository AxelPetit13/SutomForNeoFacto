package com.neofacto.sutom.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class WordManagerService implements IWordManagerService {

    private Map<Integer, List<String>> wordMap;

    public WordManagerService() {
        RestTemplate restTemplate = new RestTemplate();
        String RANDOM_WORD_API_URL = "https://random-word-api.herokuapp.com/all?lang=fr";
        String[] response = restTemplate.getForObject(RANDOM_WORD_API_URL, String[].class);

        // Sort words by length
        if (response != null) {
            this.wordMap = new HashMap<>();
            for (String word : response) {
                int length = word.length();
                if (!wordMap.containsKey(length)) {
                    wordMap.put(length, new ArrayList<>());
                }
                wordMap.get(length).add(word);
            }
        } else {
            System.out.println("The API " + RANDOM_WORD_API_URL + " returned an empty response."
                    + " Check if the Random-word-api is working and restart the application to try again.");
        }

    }

    public WordManagerService(Map<Integer, List<String>> wordMap) {
        this.wordMap = wordMap;
    }

    @Override
    public String getRandomWord(Integer wordLength) {
        List<String> words = wordMap.get(wordLength);
        if (words != null) {
            return words.get(new Random().nextInt(words.size()));
        }
        return null;
    }

    @Override
    public boolean wordExists(String word) {
        int length = word.length();
        List<String> words = wordMap.get(length);
        return words != null && words.contains(word);
    }
}
