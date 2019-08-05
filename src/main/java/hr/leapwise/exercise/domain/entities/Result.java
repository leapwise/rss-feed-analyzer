package hr.leapwise.exercise.domain.entities;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Entity
@Table(name = "RESULT")
public class Result implements Serializable {

    private static final long serialVersionUID = -6487783908039431474L;

    private Result() {};

    public Result(String code, List<Feed> feeds) {
        this.code = code;
        this.feeds = feeds;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String code;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Feed> feeds = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public Stream<Feed> getFeeds() {
       return this.feeds.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;
        Result result = (Result) o;
        return Objects.equals(id, result.id) &&
                Objects.equals(code, result.code) &&
                Objects.equals(feeds, result.feeds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, feeds);
    }
}
