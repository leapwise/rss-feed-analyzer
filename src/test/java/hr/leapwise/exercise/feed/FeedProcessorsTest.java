package hr.leapwise.exercise.feed;

import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedImpl;
import hr.leapwise.exercise.domain.processors.RomeFeedProcessor;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorException;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorExceptionMessage;
import hr.leapwise.exercise.service.FeedProcessor;
import hr.leapwise.exercise.service.FeedProcessorFactory;
import org.hamcrest.beans.HasProperty;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.collection.IsIterableWithSize;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.Every;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedProcessorsTest {

    @Autowired
    FeedProcessorFactory feedProcessorFactory;

    private FeedProcessor customTestProcessor =
            FeedProcessor.make(
                    (filePath) -> {
                        try {
                            final ClassLoader classLoader = getClass().getClassLoader();
                            File file = new File(Objects.requireNonNull(classLoader.getResource((String) filePath)).getFile());
                            final SyndFeedImpl builtFeed = (SyndFeedImpl) new SyndFeedInput().build(file);
                            return Optional.ofNullable(builtFeed);
                        } catch (FeedException | IOException e) {
                            throw new FeedProcessorException(FeedProcessorExceptionMessage.RAW_FEED_PROCESSING_EXCEPTION);
                        }
                    },
                    (rawFeed) -> { return Optional.of(new RomeFeedImpl(rawFeed)); }
          );

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void standardFeedFactoryMalformedUrlExceptionTest() {
        final FeedProcessor processor = feedProcessorFactory.create(RomeFeedProcessor.class);
        assertThat(processor, IsInstanceOf.instanceOf(RomeFeedProcessor.class));

        final String feedUrl = "hptt://feeds.bbci.co.uk/news/rss.xml";

        expectedEx.expect(FeedProcessorException.class);
        expectedEx.expectMessage(FeedProcessorExceptionMessage.INVALID_FEED_URL.setParameters(feedUrl).getMessage());

        processor.process(feedUrl);

    }

    @Test
    public void getFeedUsingFileProcessor() {
        SyndFeedImpl rawFeed = (SyndFeedImpl) customTestProcessor.process("feeds/2019_07_30_google_news_FR_rss.xml").orElse(null);
        assertNotNull(rawFeed);
        RomeFeedImpl transformedFeed =  (RomeFeedImpl) customTestProcessor.transform(rawFeed).orElse(null);

        assertThat(transformedFeed, HasPropertyWithValue.hasProperty("title", Is.is("À la une - Google Actualités")));
        assertThat(transformedFeed, HasPropertyWithValue.hasProperty("description", Is.is("Google Actualités")));
        assertThat(transformedFeed, HasPropertyWithValue.hasProperty("type", Is.is("rss_2.0")));
        assertThat(transformedFeed, HasPropertyWithValue.hasProperty("link", Is.is("https://news.google.com/?cf=all&pz=1&hl=fr&gl=FR&ceid=FR:fr")));
        assertThat(transformedFeed, HasPropertyWithValue.hasProperty("language", Is.is("fr")));
        assertThat(transformedFeed, HasPropertyWithValue.hasProperty("date",
                Is.is(Date.from(LocalDateTime.parse("2019-07-30T20:39:14.000+02:00" , DateTimeFormatter.ISO_OFFSET_DATE_TIME).atZone(ZoneId.systemDefault())
                .toInstant()))));

        assertThat(transformedFeed, HasPropertyWithValue.hasProperty("feedEntries", IsIterableWithSize.iterableWithSize(34)));
        assertThat(transformedFeed.getFeedEntries(), Every.everyItem(AllOf.allOf(HasProperty.hasProperty("title"),
                HasProperty.hasProperty("link"),
                HasProperty.hasProperty("identifier"))));

    }


}
