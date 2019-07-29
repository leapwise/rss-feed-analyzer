package hr.leapwise.exercise.domain.exceptions;


public interface ExceptionMessage<T, U extends ExceptionMessage> {
    T getMessage();

    U setParameters(Object ... args);
}
