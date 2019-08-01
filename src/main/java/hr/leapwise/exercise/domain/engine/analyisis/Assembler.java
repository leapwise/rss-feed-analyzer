package hr.leapwise.exercise.domain.engine.analyisis;

public interface Assembler<T, U> {
    U assemble(T input);
}
