/**
 * This class performs tests on the FeedSearchService class
 * @author  Ante Hakstok
 * @version 1.0
 * @since   2019-07-31
 */
package com.rssfeednewsreader;

import com.rssfeednewsreader.model.Article;
import com.rssfeednewsreader.services.FeedSearchService;
import javafx.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class TestFeedSearchService
{
    @InjectMocks
    private FeedSearchService feedSearchService;

    @Test
    public void testFeedResult()
    {

        /* First a mock collection of Articles are stored */
        List<Article> articles = new ArrayList<>();

        List<String> searchableWords1a = Arrays.asList("test1");
        List<String> searchableWords1b = Arrays.asList("test2", "test3");
        List<String> searchableWords1c = Arrays.asList("test4", "test5");
        List<String> searchableWords2a = Arrays.asList("test2", "test1", "test3");
        List<String> searchableWords2b = Arrays.asList("test1");
        List<String> searchableWords2c = Arrays.asList("test7");

        URL feedURL1a = null;
        URL feedURL1b = null;
        URL feedURL1c = null;
        URL feedURL2a = null;
        URL feedURL2b = null;
        URL feedURL2c = null;

        try
        {
            feedURL1a = new URL("https://www.test1.com");
            feedURL1b = new URL("https://www.test2.com");
            feedURL1c = new URL("https://www.test3.com");

            feedURL2a = new URL("https://www.test4.com");
            feedURL2b = new URL("https://www.test5.com");
            feedURL2c = new URL("https://www.test6.com");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        articles.add(new Article("test1", "link1a", searchableWords1a, feedURL1a));
        articles.add(new Article("test2 test3", "link1b", searchableWords1b, feedURL1b));
        articles.add(new Article("test4 test5", "link1c", searchableWords1c, feedURL1c));

        articles.add(new Article("test2 test1 test3", "link2a", searchableWords2a, feedURL2a));
        articles.add(new Article("test1", "link2a", searchableWords2b, feedURL2b));
        articles.add(new Article("test7", "link2a", searchableWords2c, feedURL2c));

        /* The service method is called for testing */
        List<Map.Entry<String, Pair<Integer, Article>>> list1 = feedSearchService.getFrequencyResults(articles);

        /* A mock comparison is created for comparison */
        Map.Entry<String, Pair<Integer, Article>> test1 = new AbstractMap.SimpleEntry<>("test1", new Pair<>(3, articles.get(0)));
        Map.Entry<String, Pair<Integer, Article>> test2 = new AbstractMap.SimpleEntry<>("test2", new Pair<>(2, articles.get(1)));
        Map.Entry<String, Pair<Integer, Article>> test3 = new AbstractMap.SimpleEntry<>("test3", new Pair<>(2, articles.get(1)));

        List<Map.Entry<String, Pair<Integer, Article>>> list2 = Arrays.asList(test1, test2, test3);

        /* The comparison is performed here */
        assertEquals(list2, list1);
    }
}
