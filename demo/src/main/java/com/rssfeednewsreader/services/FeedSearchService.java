/**
 * FeedSearchService is a service class which performs the search operation
 *
 * @author  Ante Hakstok
 * @version 1.0
 * @since   2019-07-31
 */
package com.rssfeednewsreader.services;

import com.rssfeednewsreader.model.Article;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import javafx.util.Pair;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Feed search service.
 */
@Service
public class FeedSearchService
{

    private FeedResultsService feedResultsService;

    /**
     * Instantiates the FeedSearchService class
     *
     * @param feedResultsService the FeedResultsService instance
     */
    public FeedSearchService(FeedResultsService feedResultsService) {
        this.feedResultsService = feedResultsService;
    }

    /**
     * This method is the main search method, after it performs the search through the feeds specified it will return
     * a unique ID which represents a specific search and is used later to retrieve the search results.
     *
     * @param feeds a list of all the feeds
     * @return the generated ID
     */
    public String readFeeds(List<String> feeds)
    {

        List<Map.Entry<String, Pair<Integer, Article>>> list = null;
        try
        {
            list = findResults(feeds);
        }
        catch (Exception e)
        {
            return "Something went wrong! Please perhaps check your URLs.";
        }

        /* Frequency ID is generated here*/
        Long generatedId = generateID();

        /* SaveResults is called here to store the results*/
        saveResults(list, generatedId);

        /* Generated ID is returned*/
        return generatedId.toString();
    }

    /**
     * This method is the actual method that performs the search operation; it returns all the results obtained.
     *
     * @param feeds the list of feeds
     * @return a list of results
     * @throws Exception a general exception
     */
    public List<Map.Entry<String, Pair<Integer, Article>>> findResults(List<String> feeds) throws Exception
    {
        List<Article> articles = new ArrayList<>();

        /* This is where the feed URLs are processed*/
        for (int i = 0; i < feeds.size(); i++)
        {
            try
            {
                URL feedURL = new URL(feeds.get(i));
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedURL));
                List<SyndEntry> entries = feed.getEntries();
                articles.addAll(getAllArticlesByFeed(entries, feedURL));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw e;
            }
        }
        /* This returns the results*/
        return getFrequencyResults(articles);
    }

    /**
     * This method processes the results. Two main things happening here: number of occurences for each word is
     * calculated and the results are brought down to three most occuring words.
     *
     * @param articles the Articles list
     * @return the processed results list
     */
    public List<Map.Entry<String, Pair<Integer, Article>>> getFrequencyResults(List<Article> articles) {
        HashMap<String, Pair<Integer, Article>> feedMap = new HashMap<>();

        /* Each article is checked wherever it contains a word whose frequency count we want to get. The frequency
        * count is stored as well as other values tied to the word*/
        for (Article article : articles)
        {
            for (String word : article.getSearchableWords())
            {
                if (feedMap.containsKey(word))
                {
                    if (feedMap.get(word).getValue().getFeedURL() != article.getFeedURL())
                    {
                        feedMap.put(word, new Pair<>(feedMap.get(word).getKey() + 1, feedMap.get(word).getValue()));
                    }
                }
                else
                {
                    feedMap.put(word, new Pair<>(1, article));
                }
            }
        }

        /* The results are stored in a list from which we will get only the top three results*/
        List<Map.Entry<String, Pair<Integer, Article>>> feedList = new ArrayList<>(feedMap.entrySet());
        feedList.sort(Collections.reverseOrder(Comparator.comparing(stringPairEntry -> stringPairEntry.getValue().getKey())));

        return feedList.stream().limit(3).collect(Collectors.toList());
    }

    /**
     * This method saves the results of a search.
     *
     * @param list the results list
     * @param generatedId the generated ID that represents the results
     */
    private void saveResults(List<Map.Entry<String, Pair<Integer, Article>>> list, Long generatedId)
    {
        /* The method goes through the List of results and prepares each value for database storage*/
        for (Map.Entry<String, Pair<Integer, Article>> entry : list)
        {
            String word = entry.getKey();
            Integer count = entry.getValue().getKey();
            String title = entry.getValue().getValue().getTitle();
            String link = entry.getValue().getValue().getLink();
            feedResultsService.addEntry(generatedId, word, count, title, link);
        }
    }

    /**
     * This method gets articles by feed.
     *
     * @param entries the list of tags used to differentiate nouns in word list
     * @param feedURL the url of a feed
     * @return the articles list
     */
    private List<Article> getAllArticlesByFeed(List<SyndEntry> entries, URL feedURL)
    {
        /* A tokenizer and POSTagger is preloaded in order to obtain nouns from the titles for further use*/
        List<Article> articles = new ArrayList<>();
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        InputStream inputStreamPOSTagger = getClass().getResourceAsStream("/models/en-pos-maxent.bin");
        POSModel posModel = null;
        try
        {
            posModel = new POSModel(inputStreamPOSTagger);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        POSTaggerME posTagger = new POSTaggerME(posModel);

        /* The titles are processed here; first all the non-characters are removed */
        for (SyndEntry entry : entries)
        {
            String[] tokens = tokenizer.tokenize((entry.getTitle()).replaceAll("[^a-zA-Z0-9\\s]", ""));
            /* Each word is assigned a tag which represents their word type */
            String[] tags = posTagger.tag(tokens);

            List<String> keys = new ArrayList<String>();
            for (int y = 0; y < tags.length; y++)
            {
                /* Here it checks wherever the word is a noun or not */
                if (tags[y].equals("NNP") || tags[y].equals("NN")) {
                    keys.add(tokens[y]);
                }
            }
            /* After this the titles, links, words and urls are stored to the Articles object */
            articles.add(new Article(entry.getTitle(), entry.getLink(), keys, feedURL));
        }
        return articles;
    }

    /**
     * This method generates an ID that represents a search operation
     *
     * @return the generated ID
     */
    private long generateID() {
        return (long) (Math.random() * 1000000);
    }
}
