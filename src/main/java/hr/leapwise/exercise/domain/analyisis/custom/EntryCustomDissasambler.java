package hr.leapwise.exercise.domain.analyisis.custom;

import cue.lang.WordIterator;
import cue.lang.stop.StopWords;
import hr.leapwise.exercise.domain.analyisis.Dissasambler;
import hr.leapwise.exercise.domain.analyisis.extractors.WordsExtractor;
import hr.leapwise.exercise.domain.analyisis.extractors.impl.CueWord;
import hr.leapwise.exercise.domain.analyisis.model.custom.impl.CustomItem;
import hr.leapwise.exercise.domain.feed.impl.RomeFeedEntryImpl;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EntryCustomDissasambler implements Dissasambler<RomeFeedEntryImpl, CustomItem>  {

    private static WordsExtractor<Set<CueWord>, RomeFeedEntryImpl> wordsExtractor = (e) ->
            StreamSupport.stream(new WordIterator(e.getTitle()).spliterator(), true)
                    .filter(w ->!StopWords.English.isStopWord(w))
                    .map(CueWord::new)
                    .collect(Collectors.toSet());


    @Override
    public CustomItem disassemble(final RomeFeedEntryImpl entry) {
        final CustomItem item = new CustomItem(entry.getIdentifier());
        item.extract(wordsExtractor, entry);
        item.setLink(entry.getLink());
        item.setTitle(entry.getTitle());

        return item;
    }
}
