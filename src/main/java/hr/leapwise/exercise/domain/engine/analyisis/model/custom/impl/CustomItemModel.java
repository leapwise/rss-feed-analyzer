package hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl;

import hr.leapwise.exercise.domain.engine.analyisis.extractors.Extractor;
import hr.leapwise.exercise.domain.engine.analyisis.extractors.impl.CueWord;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.abstracts.AbstractItemModel;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedEntryImpl;

import java.util.*;

public class CustomItemModel extends AbstractItemModel<String, Set<CueWord>, RomeFeedEntryImpl> {

    Set<CueWord> descriptors = new HashSet<>();

    private String title;
    private String link;

    private Set<CueWord> splitToWords(Extractor<Set<CueWord>, RomeFeedEntryImpl> wordsExtractor, final RomeFeedEntryImpl entry) {
        return wordsExtractor.extract(entry);
    }

    private CustomItemModel(String guid, String title, String link) {
        this.identitfyer = guid;
        this.title = title;
        this.link = link;
    }

    public CustomItemModel(final String guid) {
        this(guid, null, null);
    }

    @Override
    public Set<CueWord> extract(final Extractor<Set<CueWord>, RomeFeedEntryImpl> wordsExtractor, final RomeFeedEntryImpl entry) {
        this.descriptors = splitToWords(wordsExtractor, entry);
        return Collections.unmodifiableSet( this.descriptors);
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

    public String getGuid() {
        return this.identitfyer;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomItemModel)) return false;
        CustomItemModel that = (CustomItemModel) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, link);
    }
}
