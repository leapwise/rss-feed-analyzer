package hr.leapwise.exercise.domain;

public interface AbstractFactory<T> {

    T create(Class type);
}
