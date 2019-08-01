package hr.leapwise.exercise.domain.engine.feed;

import java.util.Set;

public interface Feed<T> {

    String getLanguage();

    Set<T> getFeedEntries();
}
