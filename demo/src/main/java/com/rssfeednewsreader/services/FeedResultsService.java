/**
 * FeedResultsService is a service class which contains the methods that interact with the database
 *
 * @author  Ante Hakstok
 * @version 1.0
 * @since   2019-07-31
 */
package com.rssfeednewsreader.services;

import com.rssfeednewsreader.repositories.FeedResultsRepository;
import com.rssfeednewsreader.entities.FeedResults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedResultsService
{
    private final FeedResultsRepository feedResultsRepository;

    /**
     * Instantiates the FeedResultsService class.
     *
     * @param feedResultsRepository the FeedResultsRepository instance
     */
    public FeedResultsService(FeedResultsRepository feedResultsRepository) {
        this.feedResultsRepository = feedResultsRepository;
    }

    /**
     * Adds the search results to the database
     *
     * @param uniqueId  the generated ID for the specific result
     * @param keyword   a keyword derived from a title
     * @param wordCount number of times a specific word appeared in the feeds
     * @param newsTitle the news title from which the word originated from
     * @param newsLink  the news link
     */
    public void addEntry(Long uniqueId, String keyword, Integer wordCount, String newsTitle, String newsLink)
    {
        feedResultsRepository.save(new FeedResults(uniqueId, keyword, wordCount, newsTitle, newsLink));
    }

    /**
     * Gets results.
     *
     * @param frequencyId the frequency id
     * @return the results
     */
    public List<FeedResults> getResults(Long frequencyId)
    {
        return feedResultsRepository.findAllByFrequencyId(frequencyId);
    }
}
