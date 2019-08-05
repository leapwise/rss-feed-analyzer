package hr.leapwise.exercise.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ITEM")
public class Item  extends FeedEntry {

    private static final long serialVersionUID = 2664138192013750675L;

    // Do not remove - persistence related
    private Item() { }

    public Item(final String title, final String link, final String guid) {
        this.title = title;
        this.link = link;
        this.guid = guid;
    }



}
