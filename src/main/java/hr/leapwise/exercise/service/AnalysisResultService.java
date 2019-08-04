package hr.leapwise.exercise.service;

import hr.leapwise.exercise.domain.engine.analyisis.model.Dismantled;

import java.util.Optional;

public interface AnalysisResultService<T extends Dismantled<U>, U> {

   Optional<Long> saveAnalysisResult(final T result);



}
