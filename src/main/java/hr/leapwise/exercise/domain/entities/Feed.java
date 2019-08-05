package hr.leapwise.exercise.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "FEED")
public class Feed extends FeedEntry {

    private static final long serialVersionUID = 5290735733098451946L;

    @Column
    private Instant lastBuildDate;

    @Column
    private String feedLanguage;

    // Do not remove - persistence related
    private Feed() { }

    public Feed(final String title, final String link, final String guid, final Instant lastBuildDate, final String feeLanguage) {
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.lastBuildDate = lastBuildDate;
        this.feedLanguage = feeLanguage;
    }

    public Instant getLastBuildDate() {
        return lastBuildDate;
    }

    public String getFeedLanguage() {
        return feedLanguage;
    }
}
