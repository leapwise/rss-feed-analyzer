package hr.leapwise.exercise.domain.engine.feed.impl;

import hr.leapwise.exercise.domain.engine.feed.Feed;

import java.util.Set;

public abstract class AbstractFeed<T extends AbstractFeedEntry> implements Feed<T> {

    protected Set<T> entries;

    @Override
    public Set<T> getFeedEntries() {
        return this.entries;
    }
}


