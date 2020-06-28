package com.dragutin.horvat.exercise.service;

import java.util.List;

import com.dragutin.horvat.exercise.model.NewsFeed;

public interface RssFeedParser {

	List<NewsFeed> parseRssFeedUrl(String url);

}
