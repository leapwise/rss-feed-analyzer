package hr.leapwise.exercise.domain.engine.analyisis.extractors;

@FunctionalInterface
public interface WordsExtractor<T, U> extends Extractor<T, U> {
    T extract(U input);
}
