package hr.leapwise.exercise.domain.analyisis;

public interface Analyzer<T,U> {

    T dismantle(U input);

}
