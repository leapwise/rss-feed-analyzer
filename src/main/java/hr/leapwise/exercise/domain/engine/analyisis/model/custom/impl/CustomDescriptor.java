package hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl;

import hr.leapwise.exercise.domain.engine.analyisis.extractors.impl.CueWord;

import java.util.Objects;

public class CustomDescriptor {

    private final String word;

    public CustomDescriptor(CueWord word) {
        this.word = word.getWord();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomDescriptor)) return false;
        CustomDescriptor that = (CustomDescriptor) o;
        return Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
}
