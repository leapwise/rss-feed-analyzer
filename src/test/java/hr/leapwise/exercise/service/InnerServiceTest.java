package hr.leapwise.exercise.service;

import com.sun.syndication.feed.synd.SyndFeedImpl;
import hr.leapwise.exercise.domain.engine.analyisis.Dismantler;
import hr.leapwise.exercise.domain.engine.analyisis.custom.CustomDismantler;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDismantled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDissasambled;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedImpl;
import hr.leapwise.exercise.domain.entities.Item;
import hr.leapwise.exercise.domain.entities.Result;
import hr.leapwise.exercise.feed.TestFileFeedProcessor;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InnerServiceTest {

    @Autowired
    AnalysisResultService analysisResultService;

    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts =
                    "classpath:db/cleanup.sql"),
            @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts =
                    "classpath:db/cleanup.sql")
    })
    @Test
    public void analysisServiceTest() {

        final TestFileFeedProcessor processor = new TestFileFeedProcessor();

        // TODO: Same feeds in these files are the same, only saved under different names -> how to solve this problem (establishing that feeds are the same)

        final SyndFeedImpl rawFeed1 = processor.process("feeds/2019_07_30_google_news_ENGB_rss.xml").orElse(null);
        final RomeFeedImpl transformedFeed1 = processor.transform(rawFeed1).orElse(null);
        final SyndFeedImpl rawFeed2 = processor.process("feeds/2019_07_30_google_news_ENUS_rss.xml").orElse(null);
        final RomeFeedImpl transformedFeed2 = processor.transform(rawFeed2).orElse(null);
        final Dismantler<RomeFeedImpl, CustomDissasambled> dismantler = new CustomDismantler();

        final CustomDismantled dismantled = (CustomDismantled) dismantler.dismantle(Stream.of(transformedFeed1, transformedFeed2));

        Optional<Long> resultIdFirst = analysisResultService.saveAnalysisResult(dismantled);

        final List<Item> itemsFirst = analysisResultService.getMostFrequentItems(resultIdFirst.orElse(0L));

        assertThat(itemsFirst, IsIterableWithSize.iterableWithSize(3));

        Optional<Long> resultIdSecond = analysisResultService.saveAnalysisResult(dismantled);
        assertNotEquals(resultIdFirst, resultIdSecond);

        final List<Item> itemsSecond = analysisResultService.getMostFrequentItems(resultIdSecond.orElse(0L));
        assertThat(itemsSecond, IsIterableWithSize.iterableWithSize(3));

        // results can't be compared since they will be drastically different
        //  -> that's the problem (mentioned above) if feeds are identical:
        //      - then each item will be similar to itself in another feed, and all the items will be almost equally recognized as similar
        //          - if we always take top 3 items of that kind, it's pretty big chance they will be every time different because (because of the density in that side of the histogram we use to extract most common 3)

    }
}
