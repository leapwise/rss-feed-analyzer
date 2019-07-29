package hr.leapwise.exercise;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import cue.lang.WordIterator;
import cue.lang.stop.StopWords;
import hr.leapwise.exercise.domain.feed.impl.RomeFeedImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExerciseApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Test
    public void testRssFeedReader() {

        URL feedSource = null;
        SyndFeed feed = null;
        List entries;
        RomeFeedImpl customFeed;
        try {
            feedSource = new URL("https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SyndFeedInput input = new SyndFeedInput();
        try {
            feed = input.build(new XmlReader(feedSource));

            feed.getLanguage();


             entries = feed.getEntries();

            SyndEntryImpl entry = (SyndEntryImpl) entries.get(0);
            String title = entry.getTitle();

            Map<String, Long> collected =
                    StreamSupport.stream(new WordIterator(title).spliterator(), true)
                            .filter(w ->!StopWords.English.isStopWord(w))
                            .collect(groupingBy(Function.identity(), counting()));

            customFeed = new RomeFeedImpl((SyndFeedImpl) feed);


        } catch (FeedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
