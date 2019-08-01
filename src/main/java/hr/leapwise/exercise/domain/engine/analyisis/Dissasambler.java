package hr.leapwise.exercise.domain.engine.analyisis;

@FunctionalInterface
public interface Dissasambler<T, U> {

    U disassemble(T input);

}
