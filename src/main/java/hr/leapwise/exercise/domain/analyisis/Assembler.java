package hr.leapwise.exercise.domain.analyisis;

@FunctionalInterface
public interface Assembler<T, U> {
    U assemble(T input);
}
