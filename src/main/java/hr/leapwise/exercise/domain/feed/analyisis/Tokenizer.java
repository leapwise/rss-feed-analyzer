package hr.leapwise.exercise.domain.feed.analyisis;

@FunctionalInterface
public interface Tokenizer<T, U> {
    T tokenize(U input);
}
