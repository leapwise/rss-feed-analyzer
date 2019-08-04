package hr.leapwise.exercise.domain.engine.analyisis.custom;

import cue.lang.WordIterator;
import cue.lang.stop.StopWords;
import hr.leapwise.exercise.domain.engine.analyisis.Dissasambler;
import hr.leapwise.exercise.domain.engine.analyisis.extractors.WordsExtractor;
import hr.leapwise.exercise.domain.engine.analyisis.extractors.impl.CueWord;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomItemModel;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedEntryImpl;
import hr.leapwise.exercise.domain.util.GuidUtil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EntryCustomDissasambler implements Dissasambler<RomeFeedEntryImpl, CustomItemModel> {

    private static WordsExtractor<Set<CueWord>, RomeFeedEntryImpl> wordsExtractor = (e) ->
            StreamSupport.stream(new WordIterator(e.getTitle()).spliterator(), true)
                    .filter(w ->!StopWords.English.isStopWord(w))
                    .map(CueWord::new)
                    .collect(Collectors.toSet());


    @Override
    public CustomItemModel disassemble(final RomeFeedEntryImpl entry) {
        final CustomItemModel item = new CustomItemModel(GuidUtil.getStringGuid(entry.getTitle(), entry.getLink(), entry.getIdentifier()));
        item.extract(wordsExtractor, entry);
        item.setLink(entry.getLink());
        item.setTitle(entry.getTitle());

        return item;
    }
}
