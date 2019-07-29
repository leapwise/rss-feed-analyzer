package hr.leapwise.exercise.domain.analyisis.extractors;

public interface Extractor<T,U> {
    T extract(U input);
}
