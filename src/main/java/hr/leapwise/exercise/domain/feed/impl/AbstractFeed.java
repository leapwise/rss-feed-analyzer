package hr.leapwise.exercise.domain.feed.impl;

import hr.leapwise.exercise.domain.feed.Feed;
import hr.leapwise.exercise.domain.feed.FeedEntry;
import hr.leapwise.exercise.domain.feed.analyisis.Tokenizer;

import java.util.Set;

public abstract class AbstractFeed<U, T extends AbstractFeedEntry> implements Feed<U, T> {

    protected Set<T> entries;

    @Override
    public void tokenizeEntries(Tokenizer<U, T> tokenizer) {
        for(FeedEntry e : entries) {
           e.tokenizeEntry(tokenizer);
        }
    }
}


