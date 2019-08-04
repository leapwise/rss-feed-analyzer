package hr.leapwise.exercise.domain.entities;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ANALYSIS_RESULT")
public class AnalysisResult {

    @EmbeddedId
    AnalysisBound analysisId;

    private AnalysisResult() {}

    public AnalysisResult(AnalysisBound analysisId) {
        this.analysisId = analysisId;
    }

    public Long getResultId() {
        return analysisId.getResultId();
    }

    public Long getDescriptionId() {
        return analysisId.getDescriptionId();
    }

    public Long getItemId() {
        return analysisId.getItemId();
    }

}
