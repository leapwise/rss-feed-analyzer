package canarin.boomtopic.service;

import canarin.boomtopic.domain.Feed;
import canarin.boomtopic.domain.Topic;
import canarin.boomtopic.repository.TopicRepository;
import canarin.boomtopic.util.Helpers;
import javafx.util.Pair;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Service
public class FeedService {

    private final TopicRepository topicRepository;

    /**
     * Constructor
     * @param topicRepository
     */
    public FeedService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }


    /**
     * @param feeds
     * @return generatedId
     * @throws Exception
     */
    public ResponseEntity<String> fetchFeeds(List<String> feeds) throws Exception {

        HashMap<String, Pair<Integer, Feed>> topics = null;
        List<Map.Entry<String, Pair<Integer, Feed>>> hotTopics = null;

        try
        {
            topics = analyseFeeds(feeds);

            hotTopics = new ArrayList<>(topics.entrySet());
            hotTopics.sort(Collections.reverseOrder(Comparator.comparing(stringPairEntry -> stringPairEntry.getValue().getKey())));

            hotTopics.stream().limit(3).collect(Collectors.toList());
        }
        catch (Exception e)
        {
            return  ResponseEntity.badRequest().body("Oops! Something went wrong!");
        }

        Long generatedId = Helpers.generateRandomID();
        saveTopic(hotTopics, generatedId);

        return ResponseEntity.ok(generatedId.toString());
    }

    /**
     * @param feeds
     * @return HashMap feedList with number of occurrences for each element
     * @throws Exception
     */
    public HashMap<String, Pair<Integer, Feed>> analyseFeeds(List<String> feeds) throws Exception  {

        List<Feed> feedList = new ArrayList<>();

        for ( String feed : feeds) {
            try
            {
                URL feedURL = new URL(feed);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed syndFeed = input.build(new XmlReader(feedURL));
                List<SyndEntry> entries = syndFeed.getEntries();
                feedList.addAll(findNouns(entries, feedURL));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw e;
            }
        }

        return calculateOccurrence(feedList);
    }


    /**
     * @param entries
     * @param url
     * @return List od Feeds with all nouns extracted from title
     * @throws IOException
     */
    private List<Feed> findNouns(List<SyndEntry> entries, URL url) throws IOException {

        List<Feed> feeds = new ArrayList<>();

        InputStream modelIn = null;
        POSModel POSModel = null;
        try{
            File f = new File("en-pos-maxent.bin");
            modelIn = new FileInputStream(f);
            POSModel = new POSModel(modelIn);
            POSTaggerME tagger = new POSTaggerME(POSModel);
            SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
            for (SyndEntry entry : entries)
            {

                String[] tokens = tokenizer.tokenize((entry.getTitle()));
                String[] tagged = tagger.tag(tokens);

                // Extract all nouns from title
                List<String> nouns = new ArrayList<String>();
                for (int i = 0; i < tagged.length; i++){
                    if (tagged[i].equalsIgnoreCase("nn")){ //nn is nouns singular or mass
                        nouns.add(tokens[i]);
                    }
                }

                feeds.add(new Feed(entry.getTitle(), entry.getLink(), nouns, url));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return feeds;
    }

    /**
     * Here we iterate with each feed and we try to find out how many times is each noun repeted. Each repetition is + 1
     * so that we can later sort it by its frequency
     *
     * @param feeds
     * @return number od noun occurrences
     */
    public HashMap<String, Pair<Integer, Feed>> calculateOccurrence(List<Feed> feeds) {
        HashMap<String, Pair<Integer, Feed>> feedMap = new HashMap<>();

        for (Feed feed : feeds)
        {
            for (String noun : feed.getNouns())
            {
                if (feedMap.containsKey(noun))
                {
                    feedMap.put(noun, new Pair<>(feedMap.get(noun).getKey() + 1, feedMap.get(noun).getValue()));
                }
                else
                {
                    feedMap.put(noun, new Pair<>(1, feed));
                }
            }
        }

        return feedMap;
    }

    /**
     * @param list
     * @param identifier
     * @throws MalformedURLException
     */
    private void saveTopic(List<Map.Entry<String, Pair<Integer, Feed>>> list, Long identifier) throws MalformedURLException {
        for (Map.Entry<String, Pair<Integer, Feed>> entry : list)
        {
            String word = entry.getKey();
            Integer count = entry.getValue().getKey();
            String title = entry.getValue().getValue().getTitle();
            String link = entry.getValue().getValue().getLink();
            Topic topic = new Topic(identifier,title,link,word,count);
            topicRepository.save(topic);
        }
    }
}
