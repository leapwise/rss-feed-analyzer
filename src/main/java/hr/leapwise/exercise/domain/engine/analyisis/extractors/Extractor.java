package hr.leapwise.exercise.domain.engine.analyisis.extractors;

public interface Extractor<T,U> {
    T extract(U input);
}
