/**
 * FeedSearchController is a controller class which initializes the API endpoint for the search operation
 *
 * @author  Ante Hakstok
 * @version 1.0
 * @since   2019-07-31
 */
package com.rssfeednewsreader.controllers;

import com.rssfeednewsreader.services.FeedSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedSearchController
{
    @Autowired
    private final FeedSearchService feedSearchService;

    /**
     * Instantiates the FeedSearchController class.
     *
     * @param feedSearchService the FeedSearchService instance
     */
    FeedSearchController(FeedSearchService feedSearchService) {
        this.feedSearchService = feedSearchService;
    }

    /**
     * Performs the search based on the RSS Feeds entered
     *
     * @param feeds the feeds
     * @return the ID generated for the current search results
     */
    @GetMapping("/analyse/new")
    @ResponseBody
    public String operation(@RequestParam("RSSFeed") List<String> feeds)
    {
        /* It's first necessary to check if more than two feeds were inputted. If there is only one feed, a message
         * is returned to warn the user to enter two or more feeds.*/
        if(feeds.size()< 2)
        {
            return "Not enough feeds! Please enter at least 2 feeds.";
        }
        return feedSearchService.readFeeds(feeds);
    }
}
