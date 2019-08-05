package hr.leapwise.exercise.domain.entities;

import com.sun.xml.internal.ws.api.model.SEIModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "DESCRIPTION")
public class Description implements Serializable {

    private static final long serialVersionUID = -8973086542272366172L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @NotNull
    private String code;

    private Description() {};

    public Description(final String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}
