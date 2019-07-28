package hr.leapwise.exercise.domain.feed;

import hr.leapwise.exercise.domain.feed.analyisis.Tokenizer;

public interface FeedEntry<T, U> {

   void tokenizeEntry(Tokenizer<T, U> tokenizer);
}
