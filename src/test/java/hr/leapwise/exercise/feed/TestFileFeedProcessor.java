package hr.leapwise.exercise.feed;

import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedImpl;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorException;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorExceptionMessage;
import hr.leapwise.exercise.domain.FeedProcessor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Component
public class TestFileFeedProcessor implements FeedProcessor<String, SyndFeedImpl, RomeFeedImpl> {

    @Override
    public Optional<SyndFeedImpl> process(String filePath) {

        final Optional<SyndFeedImpl> rawFeed;
        try {
            final ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource(filePath)).getFile());
            final SyndFeedImpl builtFeed = (SyndFeedImpl) new SyndFeedInput().build(file);
            rawFeed = Optional.ofNullable(builtFeed);
        } catch (FeedException | IOException e) {
            throw new FeedProcessorException(FeedProcessorExceptionMessage.RAW_FEED_PROCESSING_EXCEPTION);
        }
        return rawFeed;
    }

    @Override
    public Optional<RomeFeedImpl> transform(SyndFeedImpl rawFeed) {
        return Optional.of(new RomeFeedImpl(rawFeed));
    }
}
