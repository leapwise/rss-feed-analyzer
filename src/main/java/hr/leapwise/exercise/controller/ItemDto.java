package hr.leapwise.exercise.controller;

import hr.leapwise.exercise.domain.entities.Item;

import java.io.Serializable;

public class ItemDto implements Serializable {

    private static final long serialVersionUID = 695015137057123277L;

    private Long id;
    private String title;
    private String link;

    public ItemDto(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.link = item.getLink();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
