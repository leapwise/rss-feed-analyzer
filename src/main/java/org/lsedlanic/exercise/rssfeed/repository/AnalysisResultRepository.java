package org.lsedlanic.exercise.rssfeed.repository;

import org.lsedlanic.exercise.rssfeed.models.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, UUID> {
    List<AnalysisResult> findByAnalysisId(UUID analysisId);
}
