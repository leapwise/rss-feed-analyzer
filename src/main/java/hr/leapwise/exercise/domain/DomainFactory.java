package hr.leapwise.exercise.domain;

public interface DomainFactory<T> {

    T create(Class type);
}
