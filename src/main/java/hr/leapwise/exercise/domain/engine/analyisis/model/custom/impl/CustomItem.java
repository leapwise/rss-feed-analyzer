package hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl;

import hr.leapwise.exercise.domain.engine.analyisis.extractors.Extractor;
import hr.leapwise.exercise.domain.engine.analyisis.extractors.impl.CueWord;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.abstracts.AbstractItem;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedEntryImpl;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class CustomItem extends AbstractItem<String, Set<CueWord>, RomeFeedEntryImpl> {

    private String title;
    private String link;

    private Set<CueWord> splitToWords(Extractor<Set<CueWord>, RomeFeedEntryImpl> wordsExtractor, final RomeFeedEntryImpl entry) {
        return wordsExtractor.extract(entry);
    }

    private CustomItem(String guid, String title, String link) {
        this.identitfyer = guid;
        this.title = title;
        this.link = link;
    }

    public CustomItem(final String guid) {
        this(guid, null, null);
    }

    @Override
    public Set<CueWord> extract(final Extractor<Set<CueWord>, RomeFeedEntryImpl> wordsExtractor, final RomeFeedEntryImpl entry) {
        this.descriptors = splitToWords(wordsExtractor, entry);
        return Collections.unmodifiableSet(this.descriptors);
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<CueWord> getDescriptors() {
        return Collections.unmodifiableSet(this.descriptors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomItem)) return false;
        CustomItem that = (CustomItem) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, link);
    }
}
