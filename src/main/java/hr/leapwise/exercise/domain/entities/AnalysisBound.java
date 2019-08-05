package hr.leapwise.exercise.domain.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AnalysisBound implements Serializable {

    private static final long serialVersionUID = 7311796697124762693L;

    @Column(name = "result_id")
    private Long resultId;

    @Column(name = "description_id")
    private Long descriptionId;

    @Column(name = "item_id")
    private Long itemId;

    private AnalysisBound() {};

    public AnalysisBound(Long resultId, Long descriptionId, Long itemId) {
        this.resultId = resultId;
        this.descriptionId = descriptionId;
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisBound)) return false;
        AnalysisBound that = (AnalysisBound) o;
        return Objects.equals(resultId, that.resultId) &&
                Objects.equals(descriptionId, that.descriptionId) &&
                Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultId, descriptionId, itemId);
    }

    public Long getResultId() {
        return resultId;
    }

    public Long getDescriptionId() {
        return descriptionId;
    }

    public Long getItemId() {
        return itemId;
    }
}
