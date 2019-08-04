package hr.leapwise.exercise.domain.engine.analyisis;

public interface AnalysisInterpreter<T, U, V> {

    U analyse(T input);

    V interpret(U input);
}
