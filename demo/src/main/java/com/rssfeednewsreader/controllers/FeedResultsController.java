/**
 * FeedResultsController is a controller class which initializes the API endpoint for search results retrieval
 *
 * @author  Ante Hakstok
 * @version 1.0
 * @since   2019-07-31
 */
package com.rssfeednewsreader.controllers;

import com.rssfeednewsreader.entities.FeedResults;
import com.rssfeednewsreader.services.FeedResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedResultsController
{
    @Autowired
    private final FeedResultsService feedResultsService;

    /**
     * Instantiates the FeedResultsController class
     *
     * @param feedResultsService the FeedResultsService instance
     */
    public FeedResultsController(FeedResultsService feedResultsService)
    {
        this.feedResultsService = feedResultsService;
    }

    /**
     * Returns the search results in a JSON format
     *
     * @param id the generated ID that represents the specific search
     * @return the results in JSON format
     */
    @GetMapping("/frequency/{id}")
    @ResponseBody
    public List<FeedResults> returnJSON(@PathVariable Long id)
    {
        return feedResultsService.getResults(id);
    }
}
