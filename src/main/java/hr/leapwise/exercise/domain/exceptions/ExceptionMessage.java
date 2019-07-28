package hr.leapwise.exercise.domain.exceptions;

@FunctionalInterface
public interface ExceptionMessage<T> {

    T getMessage();
}
