package hr.leapwise.exercise.domain.analyisis.extractors.impl;

import hr.leapwise.exercise.domain.analyisis.extractors.Word;

public class CueWord implements Word {
    String word;

    public CueWord(final String word) {
        this.word = word;
    }

    public String getWord() {
        return this.word;
    }
}
