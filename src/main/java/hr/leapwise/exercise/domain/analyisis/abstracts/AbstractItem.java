package hr.leapwise.exercise.domain.analyisis.abstracts;

import hr.leapwise.exercise.domain.analyisis.model.Item;
import hr.leapwise.exercise.domain.feed.FeedEntry;

public abstract class AbstractItem<T, U, V extends FeedEntry> implements Item<U, V> {
    protected T identitfyer;
    protected U descriptors;
}