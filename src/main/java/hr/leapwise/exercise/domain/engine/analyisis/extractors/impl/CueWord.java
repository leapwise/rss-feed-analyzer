package hr.leapwise.exercise.domain.engine.analyisis.extractors.impl;

import hr.leapwise.exercise.domain.engine.analyisis.extractors.Word;

import java.util.Objects;

public class CueWord implements Word {
    String word;

    public CueWord(final String word) {
        this.word = word;
    }

    public String getWord() {
        return this.word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CueWord)) return false;
        CueWord cueWord = (CueWord) o;
        return Objects.equals(word, cueWord.word);
    }

    @Override
    public int hashCode() {

        return Objects.hash(word);
    }
}
