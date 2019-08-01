package hr.leapwise.exercise.domain.exceptions;


public interface ExceptionMessage<T> {

    T getMessage();

    ExceptionMessage setParameters(Object ... args);
}
