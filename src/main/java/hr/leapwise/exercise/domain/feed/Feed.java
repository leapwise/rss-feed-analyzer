package hr.leapwise.exercise.domain.feed;

import java.util.Set;

public interface Feed<T> {
    String getLanguage();

    Set<T> getFeedEntries();
}
