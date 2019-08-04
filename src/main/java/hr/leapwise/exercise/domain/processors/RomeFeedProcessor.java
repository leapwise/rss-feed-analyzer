package hr.leapwise.exercise.domain.processors;

import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedImpl;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorException;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorExceptionMessage;
import hr.leapwise.exercise.domain.FeedProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Component
public class RomeFeedProcessor implements FeedProcessor<String, SyndFeedImpl, RomeFeedImpl> {

    @Override
    public Optional<SyndFeedImpl> process(String url) {

        final Optional<SyndFeedImpl> rawFeed;
        if(url != null) {
            final URL feedSource;
            try {
                feedSource = new URL(url);
            } catch (MalformedURLException e) {
                throw new FeedProcessorException(FeedProcessorExceptionMessage.INVALID_FEED_URL.setParameters(url));
            }

            try {
                final SyndFeedInput input = new SyndFeedInput();
                final SyndFeedImpl builtFeed = (SyndFeedImpl) input.build(new XmlReader(feedSource));
                rawFeed = Optional.ofNullable(builtFeed);
            } catch (FeedException | IOException e) {
                throw new FeedProcessorException(FeedProcessorExceptionMessage.RAW_FEED_PROCESSING_EXCEPTION);
            }

        } else {
            rawFeed = Optional.empty();
        }
        return rawFeed;
    }

    @Override
    public Optional<RomeFeedImpl> transform(SyndFeedImpl rawFeed) {
        return Optional.of(new RomeFeedImpl(rawFeed));
    }
}
