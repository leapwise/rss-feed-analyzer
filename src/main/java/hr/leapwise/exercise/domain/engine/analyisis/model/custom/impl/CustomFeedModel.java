package hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl;

import hr.leapwise.exercise.domain.util.GuidUtil;

import java.util.Date;
import java.util.UUID;

public class CustomFeedModel {

    private String title;

    private String link;

    private Date date;

    private String language;

    private String guid;

    public CustomFeedModel(String guid, String title, String link, Date date, String language) {
        this.guid = guid;
        this.title = title;
        this.link = link;
        this.date = date;
        this.language = language;
    }

    public CustomFeedModel(String title, String link, Date date, String language) {
        this(null, link, title, date, language);

        final String guid = (link != null && title != null && date != null) ?
                (GuidUtil.getStringGuid(link, title, date.toString())) :
                UUID.randomUUID().toString();
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public Date getDate() {
        return date;
    }

    public String getLanguage() {
        return language;
    }

    public String getGuid() {
        return guid;
    }
}
