package hr.leapwise.exercise.service;

public interface AbstractFactory<T> {

    T create(Class type);
}
