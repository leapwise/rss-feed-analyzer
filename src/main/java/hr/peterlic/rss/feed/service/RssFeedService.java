package hr.peterlic.rss.feed.service;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;

import hr.peterlic.rss.feed.api.model.NewsArticle;
import hr.peterlic.rss.feed.api.request.AnalyseRequest;
import hr.peterlic.rss.feed.domain.AnalysisData;
import hr.peterlic.rss.feed.domain.NewsArticleEntity;
import hr.peterlic.rss.feed.domain.RssFeed;
import hr.peterlic.rss.feed.domain.RssFeedStatus;
import hr.peterlic.rss.feed.exception.BadRequestException;
import hr.peterlic.rss.feed.exception.DatabaseException;
import hr.peterlic.rss.feed.exception.InternalException;
import hr.peterlic.rss.feed.reader.RssFeedReader;
import hr.peterlic.rss.feed.repository.AnalysisDataRepository;
import hr.peterlic.rss.feed.repository.RssFeedRepository;
import hr.peterlic.rss.feed.util.ConstantsUtils;
import hr.peterlic.rss.feed.util.ValidatorUtils;
import hr.peterlic.rss.feed.util.WordClassifier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RssFeedService
{

	private RssFeedReader rssFeedReader;

	private WordClassifier wordClassifier;

	private RssFeedRepository rssFeedRepository;

	private AnalysisDataRepository analysisDataRepository;

	@Autowired
	public RssFeedService(RssFeedReader rssFeedReader, WordClassifier wordClassifier, RssFeedRepository rssFeedRepository,
			AnalysisDataRepository analysisDataRepository)
	{
		this.rssFeedReader = rssFeedReader;
		this.wordClassifier = wordClassifier;
		this.rssFeedRepository = rssFeedRepository;
		this.analysisDataRepository = analysisDataRepository;
	}

	/**
	 * Method that returns RSS feed guid after RSS feed is saved or throws
	 * {@link InternalException}.
	 *
	 * @param request
	 * @return
	 */
	public String returnUniqueIdentifier(AnalyseRequest request)
	{
		for (String url : request.getUrls())
		{
			ValidatorUtils.isUrlValid(url);
		}

		return saveRssFeed(request);
	}

	/**
	 * Method that stores RSS feed entry inside the database.
	 *
	 * @param request
	 * @return
	 */
	private String saveRssFeed(AnalyseRequest request)
	{
		try
		{
			RssFeed rssFeedStoredElement = rssFeedRepository.save(RssFeed.builder().urls(request.getUrls()).build());
			ValidatorUtils.notNull(rssFeedStoredElement.getGuid());
			return rssFeedStoredElement.getGuid();
		}
		catch (Exception e)
		{
			throw new InternalException(ConstantsUtils.ERROR_RSS_FEED_DATA_SAVE);
		}
	}

	/**
	 * Method that analyses URLs received from {@link AnalyseRequest}.
	 *
	 * @param request
	 * @return
	 */
	public void analyseRssFeeds(AnalyseRequest request, String guid)
	{
		RssFeed rssFeedStoredElement = null;

		try
		{
			rssFeedStoredElement = rssFeedRepository.findByGuid(guid).get();

			List<Map<String, List<NewsArticleEntity>>> listOfMapWithNouns = new ArrayList<>();

			for (String url : request.getUrls())
			{
				extractRssFeedsFromUrls(listOfMapWithNouns, url);
			}

			List<String> nounsIntersection = findIntersection(listOfMapWithNouns);
			log.info("Nouns gathered from intersection: {}", nounsIntersection);

			Map<String, List<NewsArticleEntity>> hotTopics = findHotTopics(listOfMapWithNouns, nounsIntersection);

			log.info("Hot topics map size: {}", hotTopics.size());

			saveAnalysisResult(rssFeedStoredElement, hotTopics);

			updateRssFeedStatus(rssFeedStoredElement, RssFeedStatus.SUCCESSFUL);

		}
		catch (Exception e)
		{
			log.error("Error occurred during the analysis: ", e);
			updateRssFeedStatus(rssFeedStoredElement, RssFeedStatus.FAILED);
		}
	}

	private void extractRssFeedsFromUrls(List<Map<String, List<NewsArticleEntity>>> listOfMapWithNouns, String url)
			throws IOException, FeedException, ClassNotFoundException
	{
		Map<String, List<NewsArticleEntity>> mapWithNouns = new HashMap<>();

		List<SyndEntry> feedItems = rssFeedReader.getSyndFeedEntriesFromUrl(url);

		log.info("Rss feed items size: {}", feedItems.size());
		getNounsFromTitleAndPutItInsideMap(mapWithNouns, feedItems);

		listOfMapWithNouns.add(mapWithNouns);
	}

	/**
	 * Go trough RSS article title and return only nouns.
	 * 
	 * @param mapWithNouns
	 * @param feedItems
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void getNounsFromTitleAndPutItInsideMap(Map<String, List<NewsArticleEntity>> mapWithNouns, List<SyndEntry> feedItems)
			throws IOException, ClassNotFoundException
	{
		for (SyndEntry se : feedItems)
		{
			List<String> nounsFromArticleTitle = wordClassifier.retrievePartOfSpeech(se.getTitle());
			log.info("Nouns from title: {}", nounsFromArticleTitle);
			fillMapWithNouns(mapWithNouns, se, nounsFromArticleTitle);
		}
	}

	/**
	 * Put new nouns as keys inside the map or add new {@link NewsArticleEntity} in
	 * to the list for noun if key already exist.
	 * 
	 * @param mapWithNouns
	 * @param se
	 * @param nounsFromTitle
	 */
	private void fillMapWithNouns(Map<String, List<NewsArticleEntity>> mapWithNouns, SyndEntry se, List<String> nounsFromTitle)
	{
		for (String noun : nounsFromTitle)
		{
			if (!mapWithNouns.containsKey(noun))
			{
				mapWithNouns.put(noun, new ArrayList<>());
			}

			mapWithNouns.get(noun).add(NewsArticleEntity.builder().link(se.getLink()).title(se.getTitle()).build());
		}
	}

	/**
	 * Method that finds intersection between elements provided inside lists.
	 *
	 * @param listOfMapWithNouns
	 * @return
	 */
	private List<String> findIntersection(List<Map<String, List<NewsArticleEntity>>> listOfMapWithNouns)
	{
		List<Set<String>> inputList = createSetFromMapKeys(listOfMapWithNouns);

		Set<String> nounsIntersection = new HashSet<>();

		if (CollectionUtils.isNotEmpty(inputList))
		{
			nounsIntersection = inputList.get(0);
		}

		inputList.forEach(nounsIntersection::retainAll);

		return new ArrayList<>(nounsIntersection);
	}

	private static List<Set<String>> createSetFromMapKeys(List<Map<String, List<NewsArticleEntity>>> listOfMapWithNouns)
	{
		List<Set<String>> inputList = new ArrayList<>();

		for (Map<String, List<NewsArticleEntity>> element : listOfMapWithNouns)
		{
			inputList.add(new HashSet<>(element.keySet()));
		}

		log.info("List that will be intercepted: {}", inputList);
		return inputList;
	}

	/**
	 * Returns first three hot topics depending on the frequency of noun occurrence.
	 * 
	 * @param listOfMapWithNouns
	 * @param intersectedNouns
	 * @return
	 */
	private Map<String, List<NewsArticleEntity>> findHotTopics(List<Map<String, List<NewsArticleEntity>>> listOfMapWithNouns,
			List<String> intersectedNouns)
	{
		Map<String, List<NewsArticleEntity>> hotTopics = createHotTopicsMap(listOfMapWithNouns, intersectedNouns);

		return sortHotTopicsDesc(hotTopics).entrySet().stream().limit(3).collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()),
				Map::putAll);

	}

	private Map<String, List<NewsArticleEntity>> createHotTopicsMap(List<Map<String, List<NewsArticleEntity>>> listOfMapWithNouns,
			List<String> intersectedNouns)
	{
		Map<String, List<NewsArticleEntity>> hotTopics = new HashMap<>();

		for (String noun : intersectedNouns)
		{
			hotTopics.put(noun, new ArrayList<>());

			for (Map<String, List<NewsArticleEntity>> m : listOfMapWithNouns)
			{
				hotTopics.get(noun).addAll(m.get(noun));
			}
		}
		return hotTopics;
	}

	/**
	 * Sorts hot topic map by frequency of noun occurrence in titles.
	 * 
	 * @param hotTopics
	 * @return
	 */
	private Map<String, List<NewsArticleEntity>> sortHotTopicsDesc(Map<String, List<NewsArticleEntity>> hotTopics)
	{
		Map<String, List<NewsArticleEntity>> sortedHotTopicsDesc = hotTopics.entrySet().stream()
				.sorted(comparingByValue(compareListSizeAsc().reversed())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> {
					throw new AssertionError();
				}, LinkedHashMap::new));

		logHotTopicsMapContent(sortedHotTopicsDesc);
		return sortedHotTopicsDesc;
	}

	/**
	 * Returns sorted list by size ascending.
	 * 
	 * @return
	 */
	private Comparator<List<NewsArticleEntity>> compareListSizeAsc()
	{
		return Comparator.comparingInt(List::size);
	}

	/**
	 * Logs content of map that should contain hot topics.
	 * 
	 * @param sortedHotTopicsDesc
	 */
	private void logHotTopicsMapContent(Map<String, List<NewsArticleEntity>> sortedHotTopicsDesc)
	{
		log.info("Logging the sorted map content");
		for (Map.Entry<String, List<NewsArticleEntity>> entry : sortedHotTopicsDesc.entrySet())
		{
			log.info("Len: " + entry.getValue().size() + ", " + entry.getKey() + ":" + entry.getValue().toString());
		}
	}

	/**
	 * Store result of the RSS feed analysis in to the database.
	 * 
	 * @param rssFeedStoredElement
	 * @param hotTopics
	 */
	private void saveAnalysisResult(RssFeed rssFeedStoredElement, Map<String, List<NewsArticleEntity>> hotTopics)
	{
		List<NewsArticleEntity> newsArticles = new ArrayList<>();
		for (Map.Entry<String, List<NewsArticleEntity>> entry : hotTopics.entrySet())
		{
			entry.getValue().forEach(v -> v.setRatings(entry.getValue().size()));
			newsArticles.addAll(entry.getValue());
		}

		AnalysisData analysisData = AnalysisData.builder().rssFeed(rssFeedStoredElement).topics(hotTopics.keySet())
				.newsArticles(newsArticles).build();

		newsArticles.forEach(a -> a.setAnalysisData(analysisData));

		log.info("News articles to be saved: {}", newsArticles);

		analysisDataRepository.save(analysisData);
	}

	/**
	 * Updated RSS feed entity status - SUCCESSFUL or FAILED.
	 * 
	 * @param rssFeedStoredElement
	 * @param status
	 */
	private void updateRssFeedStatus(RssFeed rssFeedStoredElement, RssFeedStatus status)
	{
		if (rssFeedStoredElement != null)
		{
			rssFeedStoredElement.setStatus(status);
			rssFeedRepository.save(rssFeedStoredElement);
		}
	}

	/**
	 * Fetches hot topic articles. Hot articles are stored during the previous RSS
	 * feed analysis.
	 * 
	 * @param guid
	 * @return
	 */
	public List<NewsArticle> fetchHotTopicRssFeedItems(String guid)
	{
		RssFeed rssFeed = validateGuid(guid);
		checkRssFeedStoredElementStatus(rssFeed);

		try
		{
			return retrieveHotTopicData(guid);
		}
		catch (Exception e)
		{
			log.error("fetchHotTopicRssFeedItems method FAILED - reason", e);
			throw new InternalException(ConstantsUtils.ERROR_INTERNAL_SERVER);
		}
	}

	/**
	 * Checks if provided GUID exist inside the database.
	 * 
	 * @param guid
	 * @return
	 */
	private RssFeed validateGuid(String guid)
	{
		Optional<RssFeed> rssFeed = rssFeedRepository.findByGuid(guid);

		if (rssFeed.isEmpty())
		{
			log.error("RSS feed for provided GUID ({}) not found in DB", guid);
			throw new BadRequestException(ConstantsUtils.ERROR_RSS_FEED_GUID_DOES_NOT_EXIST);
		}

		log.info("RSS feed found in database: {}", rssFeed.get());

		return rssFeed.get();
	}

	/**
	 * Check if analysis operation is finished - RSS feed entity should have status
	 * {@link RssFeedStatus} SUCCESSFUL.
	 * 
	 * @param rssFeed
	 */
	private void checkRssFeedStoredElementStatus(RssFeed rssFeed)
	{
		if (RssFeedStatus.IN_PROGRESS.equals(rssFeed.getStatus()))
		{
			log.error("RSS feed status - IN_PROGRESS");
			throw new DatabaseException(ConstantsUtils.ERROR_RSS_FEED_STATUS_IN_PROGRESS);
		}
		else if (RssFeedStatus.FAILED.equals(rssFeed.getStatus()))
		{
			log.error("RSS feed status - FAILED");
			throw new DatabaseException(ConstantsUtils.ERROR_RSS_STATUS_FEED_FAILED);
		}
	}

	/**
	 * Fetches hot topic data from database using the GUID provided from the
	 * request.
	 * 
	 * @param guid
	 * @return
	 */
	private List<NewsArticle> retrieveHotTopicData(String guid)
	{
		List<NewsArticle> newsArticleList = new ArrayList<>();

		AnalysisData analysisData = analysisDataRepository.findOneByRssFeed_Guid(guid);
		List<NewsArticleEntity> hotTopicArticles = analysisData.getNewsArticles();

		log.info("Hot topic articles: {}", hotTopicArticles);

		createListOfArticlesToReturn(newsArticleList, hotTopicArticles);
		return newsArticleList;
	}

	/**
	 * Creates list of articles and sort list items by their ratings - frequency of
	 * occurrence
	 * 
	 * @param newsArticleList
	 * @param hotTopicArticles
	 */
	private void createListOfArticlesToReturn(List<NewsArticle> newsArticleList, List<NewsArticleEntity> hotTopicArticles)
	{
		for (NewsArticleEntity article : hotTopicArticles)
		{
			NewsArticle articleApi = new NewsArticle();
			BeanUtils.copyProperties(article, articleApi);
			newsArticleList.add(articleApi);
		}

		newsArticleList.sort(Comparator.comparing(NewsArticle::getRatings).reversed());
	}

}
