package hr.leapwise.exercise.domain.analyisis.model;

import hr.leapwise.exercise.domain.analyisis.extractors.Extractor;

public interface Item<T, U> {

    T extract(Extractor<T, U> extractor, U entry);
}
