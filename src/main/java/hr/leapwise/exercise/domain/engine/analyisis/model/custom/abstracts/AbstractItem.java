package hr.leapwise.exercise.domain.engine.analyisis.model.custom.abstracts;

import hr.leapwise.exercise.domain.engine.analyisis.model.Item;
import hr.leapwise.exercise.domain.engine.feed.FeedEntry;

public abstract class AbstractItem<T, U, V extends FeedEntry> implements Item<U, V> {
    protected T identitfyer;
    protected U descriptors;
}