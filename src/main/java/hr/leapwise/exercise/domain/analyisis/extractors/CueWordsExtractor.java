package hr.leapwise.exercise.domain.analyisis.extractors;

import hr.leapwise.exercise.domain.feed.FeedEntry;

import java.util.Set;

public interface CueWordsExtractor<T extends Word, U extends FeedEntry> extends WordsExtractor<Set<T>, U> {

    @Override
    Set<T> extract(U entry);
}
