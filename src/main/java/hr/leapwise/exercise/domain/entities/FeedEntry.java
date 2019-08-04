package hr.leapwise.exercise.domain.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class FeedEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(updatable=false)
    @NotNull
    protected String title;

    @Column(updatable=false)
    @NotNull
    protected String link;

    @Column(updatable=false)
    @NotNull
    protected String guid;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getGuid() {
        return guid;
    }

}
