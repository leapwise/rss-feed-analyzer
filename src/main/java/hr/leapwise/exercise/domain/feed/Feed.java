package hr.leapwise.exercise.domain.feed;

import hr.leapwise.exercise.domain.feed.analyisis.Tokenizer;

public interface Feed<T, U> {

    void tokenizeEntries(Tokenizer<T, U> tokenizer);
}
