package hr.leapwise.exercise.service.impl;

import hr.leapwise.exercise.dao.AnalysisRepository;
import hr.leapwise.exercise.dao.DescriptionRepository;
import hr.leapwise.exercise.dao.ItemRepository;
import hr.leapwise.exercise.dao.ResultRepository;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.*;
import hr.leapwise.exercise.domain.entities.*;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorException;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorExceptionMessage;
import hr.leapwise.exercise.service.AnalysisResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomAnalysisResultService implements AnalysisResultService<CustomDismantled, CustomDissasambled> {

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private DescriptionRepository descriptionRepository;

    @Autowired
    private ItemRepository itemRepository;

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
}
