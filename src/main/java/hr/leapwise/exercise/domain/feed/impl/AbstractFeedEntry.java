package hr.leapwise.exercise.domain.feed.impl;

import hr.leapwise.exercise.domain.feed.FeedEntry;


public abstract class AbstractFeedEntry<T> implements FeedEntry {

    protected T identitfier;
}
