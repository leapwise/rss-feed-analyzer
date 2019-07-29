package hr.leapwise.exercise.domain.analyisis.custom;

import hr.leapwise.exercise.domain.analyisis.Dissasambler;
import hr.leapwise.exercise.domain.analyisis.extractors.impl.CueWord;
import hr.leapwise.exercise.domain.analyisis.model.custom.impl.CustomDescriptor;
import hr.leapwise.exercise.domain.analyisis.model.custom.impl.CustomItem;
import hr.leapwise.exercise.domain.feed.exceptions.FeedException;
import hr.leapwise.exercise.domain.feed.exceptions.FeedExceptionMessageCode;
import hr.leapwise.exercise.domain.feed.impl.RomeFeedEntryImpl;
import hr.leapwise.exercise.domain.feed.impl.RomeFeedImpl;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class FeedCustomDissasambler implements Dissasambler<RomeFeedImpl, Map<CustomDescriptor, Set<CustomItem>>> {

    private Set<CueWord> descriptors = new HashSet<>();
    
    @Override
    public Map<CustomDescriptor, Set<CustomItem>> disassemble(RomeFeedImpl feed) {

        Map<CustomDescriptor, Set<CustomItem>> significantDescriptors = new HashMap<>();

        final String feedLanguage = feed.getLanguage();
        if (feedLanguage != null && feedLanguage.matches("(?i)(^EN-.+|.+-EN$)")){

            final EntryCustomDissasambler entryDismantler = new EntryCustomDissasambler();
            Set<CueWord> itemDescriptors;
            CustomItem item;

            for (RomeFeedEntryImpl entry : feed.getFeedEntries()) {
                item = entryDismantler.disassemble(entry);
                itemDescriptors = item.getDescriptors();

                if (!CollectionUtils.isEmpty(itemDescriptors)) {

                    if (!CollectionUtils.isEmpty(descriptors)) {

                        Set<CueWord> intersection = new HashSet<>(descriptors); // use the copy constructor
                        intersection.retainAll(itemDescriptors);
                        if (!CollectionUtils.isEmpty(intersection)) {

                            for(CueWord word : intersection) {
                                significantDescriptors.put(new CustomDescriptor(word), Collections.singleton(item));
                            }
                        }
                    }
                    // Assumption here is that descriptors can be held in memory since their number should not be significantly high:
                    //      - we only take words from titles, and feeds don't have that much of entries.
                    descriptors.addAll(itemDescriptors);
                }
            }
        } else {
            // This implementation supports only English (it's one of the simplifications):
            //  - there is the possibility, though, to make it multilingual -> making factory for retrieving appropriate WordsExtractor
            //          - but, take notice that in that case, problem of possibility to have multilingual entries still remins, and should be handled in it's own manner
            // Also, assumption is made that all the items in the feed are in the same language:
            //  - those that are not will simply not be analysed
            throw new FeedException(FeedExceptionMessageCode.UNSUPPORTED_LANGUAGE.setParameters(feedLanguage));
        }
        return Collections.unmodifiableMap(significantDescriptors);
    }
}
