package hr.leapwise.exercise.domain.engine.analyisis.model;

import hr.leapwise.exercise.domain.engine.analyisis.extractors.Extractor;

public interface ItemModel<T, U> {

    T extract(Extractor<T, U> extractor, U entry);
}
