package hr.leapwise.exercise.service.impl;

import com.sun.syndication.feed.synd.SyndFeedImpl;
import hr.leapwise.exercise.domain.engine.analyisis.Dismantler;
import hr.leapwise.exercise.domain.engine.analyisis.custom.CustomDismantler;
import hr.leapwise.exercise.domain.engine.analyisis.exceptions.AnalyisisExceptionMessage;
import hr.leapwise.exercise.domain.engine.analyisis.exceptions.AnalysisException;
import hr.leapwise.exercise.domain.engine.analyisis.model.Dismantled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDismantled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDissasambled;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedImpl;
import hr.leapwise.exercise.domain.entities.AnalysisResult;
import hr.leapwise.exercise.domain.entities.Item;
import hr.leapwise.exercise.domain.processors.FeedProcessor;
import hr.leapwise.exercise.domain.processors.FeedProcessorFactory;
import hr.leapwise.exercise.domain.processors.RomeFeedProcessor;
import hr.leapwise.exercise.service.AnalysisResultService;
import hr.leapwise.exercise.service.FeedAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomFeedAnalysisService implements FeedAnalysisService {

    @Autowired
    private AnalysisResultService<CustomDismantled, CustomDissasambled, Item> analysisRepositoryService;

    @Autowired
    FeedProcessorFactory feedProcessorFactory;

    private List<RomeFeedImpl> parseFeeds(String[] feedUrls) {
        List<RomeFeedImpl> feeds = new ArrayList<>();

        if (Optional.ofNullable(feedUrls).map(urls -> urls.length).orElse(0) > 1) {
            final FeedProcessor<String, SyndFeedImpl, RomeFeedImpl>  processor = feedProcessorFactory.create(RomeFeedProcessor.class);

            for (String url : feedUrls) {
                Optional<SyndFeedImpl> rawFeed = processor.process(url);
                if (rawFeed.isPresent()) {
                    Optional<RomeFeedImpl> transformedFeed = processor.transform(rawFeed.get());
                    transformedFeed.ifPresent(feeds::add);
                }
            }
        } else {
            throw new AnalysisException(AnalyisisExceptionMessage.AT_LEAST_2_INPUTS_REQUIRED);
        }
        return feeds;
    }

    private CustomDismantled dismantleFeeds(List<RomeFeedImpl> feeds) {
        final Dismantler<RomeFeedImpl, CustomDissasambled> dismantler = new CustomDismantler();
        return (CustomDismantled) dismantler.dismantle(feeds.stream());
    }

    @Override
    public Long analyseFeeds(final String[] feedUrls) {

        final CustomDismantled dismantled = dismantleFeeds(parseFeeds(feedUrls));
        Optional<Long> resultId = analysisRepositoryService.saveAnalysisResult(dismantled);

        return resultId.orElse(null);
    }

    @Override
    public List<Item> getAnalysisResult(final Long analysisResultId) {
        return Optional.ofNullable(analysisRepositoryService.getMostFrequentItems(analysisResultId)).orElse(new ArrayList<>());
    }



}
