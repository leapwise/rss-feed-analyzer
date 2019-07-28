package hr.leapwise.exercise.domain.feed.impl;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import hr.leapwise.exercise.domain.feed.analyisis.Tokenizer;

import java.util.Set;

public class RomeFeedEntryImpl extends AbstractFeedEntry<String, Set<String>, RomeFeedEntryImpl> {

    private final String title;
    private final String link;


    public RomeFeedEntryImpl(SyndEntryImpl entry) {
        this.identitfier = entry.getUri();
        this.title = entry.getTitle();
        this.link = entry.getLink();
    }

    @Override
    public void tokenizeEntry(Tokenizer<Set<String>, RomeFeedEntryImpl> tokenizer) {
        this.tokenizedEntry = tokenizer.tokenize(this);
    }

    public String getTitle() {
        return title;
    }
}
