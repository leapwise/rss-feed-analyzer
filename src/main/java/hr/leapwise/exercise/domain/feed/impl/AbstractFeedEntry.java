package hr.leapwise.exercise.domain.feed.impl;

import hr.leapwise.exercise.domain.feed.FeedEntry;
import hr.leapwise.exercise.domain.feed.analyisis.Tokenizer;


public abstract class AbstractFeedEntry<T, U, V extends FeedEntry> implements FeedEntry<U, V> {

    protected T identitfier;
    protected U tokenizedEntry;

    @Override
    public abstract void tokenizeEntry(Tokenizer<U, V> tokenizer);
}
