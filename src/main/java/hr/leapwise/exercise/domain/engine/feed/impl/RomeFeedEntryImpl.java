package hr.leapwise.exercise.domain.engine.feed.impl;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import hr.leapwise.exercise.domain.util.GuidUtil;

import java.util.Optional;

public class RomeFeedEntryImpl extends AbstractFeedEntry<String> {

    private final String title;
    private final String link;


    public RomeFeedEntryImpl(SyndEntryImpl entry) {
        this.title = entry.getTitle();
        this.link = entry.getLink();
        this.identitfier = Optional.ofNullable(entry.getUri()).orElse(GuidUtil.getStringGuid(link, title));
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
