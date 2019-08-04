package hr.leapwise.exercise.dao;

import hr.leapwise.exercise.domain.entities.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnalysisRepository extends JpaRepository<AnalysisResult, Long> {


    @Query(value = "SELECT a.* FROM ANALYSIS_RESULT a" +
            "   INNER JOIN ITEM i ON a.item_id = i.id" +
            "   INNER JOIN DESCRIPTION d ON a.description_id = d.id " +
            "   WHERE a.result_id = :result_id", nativeQuery = true)
    List<AnalysisResult> findAllByResultId(@Param("result_id") Long resultId);

}
