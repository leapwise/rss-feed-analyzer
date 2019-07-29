package hr.leapwise.exercise.domain.feed.impl;

import com.sun.syndication.feed.synd.SyndEntryImpl;

public class RomeFeedEntryImpl extends AbstractFeedEntry<String> {

    private final String title;
    private final String link;

    public RomeFeedEntryImpl(SyndEntryImpl entry) {
        this.identitfier = entry.getUri();
        this.title = entry.getTitle();
        this.link = entry.getLink();
    }

    public String getTitle() {
        return this.title;
    }

    public String getIdentifier() {
        return this.identitfier;
    }

    public String getLink() {
        return link;
    }
}
