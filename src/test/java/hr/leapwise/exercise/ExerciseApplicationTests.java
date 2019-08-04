package hr.leapwise.exercise;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import hr.leapwise.exercise.dao.ResultRepository;
import hr.leapwise.exercise.domain.engine.analyisis.AnalyserProperties;
import hr.leapwise.exercise.domain.engine.analyisis.Dismantler;
import hr.leapwise.exercise.domain.engine.analyisis.custom.CustomDismantler;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDismantled;
import hr.leapwise.exercise.domain.engine.analyisis.model.custom.impl.CustomDissasambled;
import hr.leapwise.exercise.domain.engine.feed.impl.RomeFeedImpl;
import hr.leapwise.exercise.domain.entities.Result;
import hr.leapwise.exercise.domain.processors.exceptions.FeedProcessorException;
import hr.leapwise.exercise.service.AnalysisResultService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestConfiguration
public class ExerciseApplicationTests {

    @Autowired
    AnalysisResultService<CustomDismantled, CustomDissasambled> analysisResultService;

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    AnalyserProperties analyserProperties;

    @Test
    public void contextLoads() {
    }


    @Test
    public void testRssFeedReader() {

        List<String> feedUrls = new ArrayList<>();
        List<RomeFeedImpl> feeds = new ArrayList<>();
        final Dismantler<RomeFeedImpl, CustomDissasambled> dismantler = new CustomDismantler();
        feedUrls.add("https://news.google.com/rss?cf=all&pz=1&hl=en-GB&gl=GB&ceid=GB:en");
        feedUrls.add("https://news.google.com/rss?cf=all&pz=1&hl=en-US&gl=US&ceid=US:en");
        feedUrls.add("http://feeds.bbci.co.uk/news/rss.xml");
        // http://feeds.bbci.co.uk/news/rss.xml

        for (String url : feedUrls) {
            URL feedSource = null;
            SyndFeed feed = null;
            RomeFeedImpl customFeed;
            try {
                feedSource = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            SyndFeedInput input = new SyndFeedInput();
            try {
                feed = input.build(new XmlReader(feedSource));
                customFeed = new RomeFeedImpl((SyndFeedImpl) feed);
                feeds.add(customFeed);
            } catch (FeedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<Result> results;
        final CustomDismantled dismantled = (CustomDismantled) dismantler.dismantle(feeds.stream());
        try {
            analysisResultService.saveAnalysisResult(dismantled);
        } catch (FeedProcessorException e) {

            results = resultRepository.findAll();
            for (Result result : results) {
               result.getFeeds().forEach(
                       f -> System.out.println(f.getTitle())
               );
            }
        }


    }

}
