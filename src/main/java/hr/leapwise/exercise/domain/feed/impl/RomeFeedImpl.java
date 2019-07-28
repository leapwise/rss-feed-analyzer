package hr.leapwise.exercise.domain.feed.impl;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import cue.lang.WordIterator;
import cue.lang.stop.StopWords;
import hr.leapwise.exercise.domain.feed.exceptions.FeedException;
import hr.leapwise.exercise.domain.feed.exceptions.FeedExceptionMessageCode;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RomeFeedImpl extends AbstractFeed<Set<String>, RomeFeedEntryImpl> {

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

    public void tokenizeTitles() {

        if (this.language != null && this.language.matches("(?i)(^EN-.+|.+-EN$)")){
            super.tokenizeEntries((e) ->
                    StreamSupport.stream(new WordIterator(e.getTitle()).spliterator(), true)
                            .filter(w ->!StopWords.English.isStopWord(w))
                            .collect(Collectors.toSet())

            );

        } else {
            // This implementation supports only English
            // Also, assumption is made that all the items in the feed are in the same language:
            //  - those that are not will simply not be analysed
            throw new FeedException(FeedExceptionMessageCode.UNSUPPORTED_LANGUAGE);
        }

    }
}
