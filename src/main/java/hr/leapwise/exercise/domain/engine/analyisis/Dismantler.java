package hr.leapwise.exercise.domain.engine.analyisis;

import hr.leapwise.exercise.domain.engine.analyisis.model.Dismantled;

import java.util.stream.Stream;

@FunctionalInterface
public interface Dismantler<T, U>{

    Dismantled<U> dismantle(Stream<T> input);
}
