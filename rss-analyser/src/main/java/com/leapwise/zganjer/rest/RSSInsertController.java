package com.leapwise.zganjer.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.leapwise.zganjer.service.RSSService;

@RestController
@RequestMapping("/analyse/new")
public class RSSInsertController {

	@Autowired
	RSSService rssService;
	
	@RequestMapping(path = "", method = RequestMethod.POST)
	public String insertRssFeed(
		@RequestParam(value = "Rss1") String rssFeed1,
		@RequestParam(value = "Rss2") String rssFeed2,
		@RequestParam(value = "Rss3", required = false) String rssFeed3,
		@RequestParam(value = "Rss4", required = false) String rssFeed4) throws Exception {	
		List<String> inputFeeds = Arrays.asList(rssFeed1,rssFeed2,rssFeed3,rssFeed4);
		return rssService.processRSSFeed(inputFeeds);		
	}
}
