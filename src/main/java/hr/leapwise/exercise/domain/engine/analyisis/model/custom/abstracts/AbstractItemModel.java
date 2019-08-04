package hr.leapwise.exercise.domain.engine.analyisis.model.custom.abstracts;

import hr.leapwise.exercise.domain.engine.analyisis.model.ItemModel;
import hr.leapwise.exercise.domain.engine.feed.FeedEntry;

public abstract class AbstractItemModel<T, U, V extends FeedEntry> implements ItemModel<U, V> {
    protected T identitfyer;
}