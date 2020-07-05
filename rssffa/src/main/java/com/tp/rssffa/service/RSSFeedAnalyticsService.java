package com.tp.rssffa.service;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.rssffa.constants.MessagesConstants;
import com.tp.rssffa.dao.RSSFeedDAO;
import com.tp.rssffa.exception.RSSFeedException;
import com.tp.rssffa.model.RSSFeedAnalytics;
import com.tp.rssffa.model.RSSFeedAnalyticsDto;
import com.tp.rssffa.util.AnalyzeHelper;
import com.tp.rssffa.util.ValidationUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for processing RSS feed and retrieving processed feed from DB.
 * 
 * @author Tihomir
 *
 */
@Service
@Slf4j
public class RSSFeedAnalyticsService {

	@Autowired
	private DozerBeanMapper dozerBeanMapper;

	@Autowired
	private RSSFeedDAO rssFeedDAO;

	@Autowired
	private RSSFeedReaderService rSSFeedReader;

	/**
	 * Method for analyze RSS feed from URL.
	 * 
	 * @param urls
	 * @return uuid -
	 */
	public String analyse(List<String> urls) {
		ValidationUtil.validateRequest(urls);

		String uuid = UUID.randomUUID().toString();
		List<RSSFeedAnalytics> listOfRssFeeds = rSSFeedReader.getRSSFeedsFromURLs(urls);

		log.info("Feeds to analyze = " + listOfRssFeeds.size());
		List<RSSFeedAnalytics> listOfAnalizedData = analyzeData(listOfRssFeeds);
		if(listOfAnalizedData.isEmpty())
			throw new RSSFeedException(MessagesConstants.NO_DATA);
		listOfAnalizedData.forEach(data -> setUuidAndSave(data, uuid));

		return uuid;
	}

	/**
	 * Setting uuid and saving object.
	 * 
	 * @param data - objects containing title and URL
	 * @param uuid - unique id for request
	 */
	private void setUuidAndSave(RSSFeedAnalytics data, String uuid) {
		log.info(data.toString());
		data.setUuid(uuid);
		rssFeedDAO.save(data);
	}

	/**
	 * Method for analyzing raw data. We read all words from titles and count them
	 * to find most frequently used word. Then we pick 3 most used words. With that
	 * words we create map with key of one the word and value as list of items who
	 * have that word in title. Keys are inserted in reserve order.
	 * 
	 * Then we iterate through map and pick 3 data.
	 * 
	 * @param listOfRssFeeds - list of raw data with titles and urls
	 * @return - return top 3 data
	 */
	private List<RSSFeedAnalytics> analyzeData(List<RSSFeedAnalytics> listOfRssFeeds) {
		Map<String, Integer> listOfCountedWords = AnalyzeHelper.countWordsAndCreateMapOfThem(listOfRssFeeds);

		List<String> topWords = AnalyzeHelper.searchForTopThreeWords(listOfCountedWords);
		log.info("Top words in feed = ");
		topWords.forEach(data -> log.info(data + "\n"));
		
		Map<String, List<RSSFeedAnalytics>> map = new LinkedHashMap<String, List<RSSFeedAnalytics>>();
		for (String topWord : topWords) {
			map.put(topWord, AnalyzeHelper.getValuesForMap(listOfRssFeeds, topWord));
		}

		return AnalyzeHelper.getListOfThreeData(map);
	}

	/**
	 * Method for retrieving data from DB with unique id.
	 * 
	 * @param id - unique id
	 * @return - list of dto data
	 */
	
	public List<RSSFeedAnalyticsDto> getDataForUuid(String id) {
		List<RSSFeedAnalytics> listOfDataFromDatabase = rssFeedDAO.findByUuid(id);
		return listOfDataFromDatabase
				.stream()
				.map(e -> dozerBeanMapper.map(e, RSSFeedAnalyticsDto.class))
				.collect(toList());
	}

}
