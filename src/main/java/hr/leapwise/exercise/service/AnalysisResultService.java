package hr.leapwise.exercise.service;

import hr.leapwise.exercise.domain.engine.analyisis.model.Dismantled;

import java.util.List;
import java.util.Optional;

public interface AnalysisResultService<T extends Dismantled<U>, U, V> {

   Optional<Long> saveAnalysisResult(final T dismantled);

   List<V> getMostFrequentItems(final Long analysisResultId);

}
