package hr.leapwise.exercise.domain.feed.impl;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import hr.leapwise.exercise.domain.analyisis.extractors.impl.CueWord;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class RomeFeedImpl extends AbstractFeed<RomeFeedEntryImpl, Set<CueWord>> {

    final private String title;
    final private String description;
    final private String type;
    final private String link;
    final private String language;
    final private Date date;

    public RomeFeedImpl(SyndFeedImpl feed) {
        if (feed != null) {

            this.title = feed.getTitle();
            this.description = feed.getDescription();
            this.type = feed.getFeedType();
            this.link = feed.getLink();
            this.language = feed.getLanguage();
            this.date = feed.getPublishedDate();


            final Set<RomeFeedEntryImpl> entries = new HashSet<>();
            final List rawEntries = feed.getEntries();
            if (!CollectionUtils.isEmpty(rawEntries)) {
                for (int i = 0; i < rawEntries.size(); i++) {
                    if (SyndEntryImpl.class.isInstance(rawEntries.get(i))) {
                        entries.add(new RomeFeedEntryImpl((SyndEntryImpl) rawEntries.get(i)));
                    }

                }
            }
            this.entries = entries;
        } else {
            this.title = null;
            this.description = null;
            this.type = null;
            this.link = null;
            this.language = null;
            this.date = null;
            this.entries = new HashSet<>();
        }
    }

    @Override
    public String getLanguage() {
        return this.language;
    }
}
