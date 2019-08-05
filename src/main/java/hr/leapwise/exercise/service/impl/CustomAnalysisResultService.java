package hr.leapwise.exercise.service.impl;

import hr.leapwise.exercise.dao.AnalysisRepository;
import hr.leapwise.exercise.dao.DescriptionRepository;
import hr.leapwise.exercise.dao.ItemRepository;
import hr.leapwise.exercise.dao.ResultRepository;
import hr.leapwise.exercise.domain.engine.analyisis.AnalysisDomainFactory;
import hr.leapwise.exercise.domain.engine.analyisis.AnalysisInterpreter;
import hr.leapwise.exercise.domain.engine.analyisis.custom.CustomAnalysisInterpreter;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDescriptor;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDismantled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDissasambled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomItemModel;
import hr.leapwise.exercise.domain.entities.*;
import hr.leapwise.exercise.service.AnalysisResultService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomAnalysisResultService implements AnalysisResultService<CustomDismantled, CustomDissasambled, Item> {

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AnalysisDomainFactory analysisDomainFactory;

    @Override
    @Transactional
    public Optional<Long> saveAnalysisResult(CustomDismantled dismantled) {
        final Optional<Long> resultIdOptional;

        Map<CustomDescriptor, Set<CustomItemModel>> significantDescriptors = dismantled.getSignificantDescriptors();
        if (!significantDescriptors.isEmpty()) {

            final List<Feed> feeds = dismantled.getFeeds().stream().map(fm -> new Feed(fm.getTitle(), fm.getLink(), fm.getGuid(), fm.getDate().toInstant(), fm.getLanguage())).collect(Collectors.toList());

            final Result result = new Result(UUID.randomUUID().toString(), feeds);

            // saves new result and related feeds
            resultRepository.save(result);

            significantDescriptors.forEach((key, value) -> {

                final Description description = new Description(key.getWord());
                descriptionRepository.save(description);


                for(CustomItemModel im : value) {
                    Optional<Item> itemOptional = itemRepository.findOneByGuid(im.getGuid());

                    final Item item;
                    item = itemOptional.orElseGet(() -> itemRepository.save(new Item(im.getTitle(), im.getLink(), im.getGuid())));

                    final AnalysisBound bound = new AnalysisBound(result.getId(), description.getId(), item.getId());
                    analysisRepository.save(new AnalysisResult(bound));
                }

            });
            resultIdOptional = Optional.of(result.getId());
        } else {
            resultIdOptional = Optional.empty();
        }
        return resultIdOptional;
    }

    @Override
    public List<Item> getMostFrequentItems(final Long analysisResultId) {
        List<Item> items = new ArrayList<>();

        if (analysisResultId != null) {

            List<AnalysisResult> analysisResults = analysisRepository.findAllByResultId(analysisResultId);

            if (!analysisResults.isEmpty()) {
                final AnalysisInterpreter<List<Pair<Long, Long>>, Map<Pair<Long, Long>, Map<Long, Set<Long>>>, List<Long>> interpreter =
                        analysisDomainFactory.create(CustomAnalysisInterpreter.class);

                List<Long> mostFrequentItems = interpreter.interpret(interpreter.analyse(analysisResults.stream().map(ar -> Pair.of(ar.getDescriptionId(), ar.getItemId())).collect(Collectors.toList())));

                if (!mostFrequentItems.isEmpty()) {
                    items = itemRepository.findAllById(mostFrequentItems);
                }
            }

        }
        return items;
    }
}
