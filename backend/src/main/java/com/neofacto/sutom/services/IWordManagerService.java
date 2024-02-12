package com.neofacto.sutom.services;

public interface IWordManagerService {

    String getRandomWord(Integer wordLength);

    boolean wordExists(String word);
}
