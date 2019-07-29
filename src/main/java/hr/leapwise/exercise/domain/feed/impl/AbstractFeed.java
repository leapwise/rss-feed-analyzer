package hr.leapwise.exercise.domain.feed.impl;

import hr.leapwise.exercise.domain.feed.Feed;

import java.util.Set;

public abstract class AbstractFeed<T extends AbstractFeedEntry> implements Feed<T> {

    protected Set<T> entries;

    protected Set<T> getEntries() {
        return this.entries;
    }
}


