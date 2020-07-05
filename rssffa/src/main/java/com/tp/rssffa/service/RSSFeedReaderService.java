package com.tp.rssffa.service;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.tp.rssffa.constants.MessagesConstants;
import com.tp.rssffa.exception.RSSFeedException;
import com.tp.rssffa.model.RSSFeedAnalytics;

@Service
public class RSSFeedReaderService {

	public List<RSSFeedAnalytics> getRSSFeedsFromURLs(List<String> urls) {
		return urls.stream().map(e -> getFeeds(e)).flatMap(List::stream).collect(toList());
	}

	private static List<RSSFeedAnalytics> getFeeds(String url) {
		List<SyndEntry> listOfFeeds = new ArrayList<>();
		try {
			listOfFeeds = getSyndEntries(url);
		} catch (Exception e) {
			throw new RSSFeedException(MessagesConstants.ERROR_RETRIEVING_DATA + url);
		}
		if(listOfFeeds.isEmpty()){
			throw new RSSFeedException(MessagesConstants.NO_DATA_TO_ANALYZE);
		}
		return listOfFeeds.parallelStream()
				.map(RSSFeedAnalytics::getRSSFeedAnalyticsFromSyndEntry)
				.collect(toList());
	}

	private static List<SyndEntry> getSyndEntries(String url) throws FeedException, IOException {
		URL feedSource = new URL(url);
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(feedSource));
		return feed.getEntries();
	}
}
