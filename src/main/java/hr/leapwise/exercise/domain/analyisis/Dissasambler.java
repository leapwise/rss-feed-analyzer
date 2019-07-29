package hr.leapwise.exercise.domain.analyisis;

@FunctionalInterface
public interface Dissasambler<T, U> {

    U disassemble(T input);

}
