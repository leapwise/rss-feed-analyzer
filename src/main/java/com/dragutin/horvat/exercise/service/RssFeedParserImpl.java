package com.dragutin.horvat.exercise.service;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dragutin.horvat.exercise.model.NewsFeed;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@Service
public class RssFeedParserImpl implements RssFeedParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(RssFeedParserImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<NewsFeed> parseRssFeedUrl(String url) {

		SyndFeedInput input = new SyndFeedInput();
		List<NewsFeed> newsFeedList = new ArrayList<>();
		try {

			SyndFeed feed = input.build(new XmlReader(new URL(url)));

			for (SyndEntryImpl entry : (List<SyndEntryImpl>) feed.getEntries()) {
				NewsFeed newsFeed = new NewsFeed();
				newsFeed.setTitle(entry.getTitle());
				newsFeed.setUrl(entry.getLink());

				LOGGER.info("Found news {}", newsFeed);
				newsFeedList.add(newsFeed);
			}
		} catch (IllegalArgumentException | FeedException | IOException e) {
			e.printStackTrace();
		}
		return newsFeedList;
	}

}
