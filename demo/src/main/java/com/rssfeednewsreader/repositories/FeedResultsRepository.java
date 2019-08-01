/**
 * FeedResultsRepository is a repository where the database interaction methods are stored
 *
 * @author  Ante Hakstok
 * @version 1.0
 * @since   2019-07-31
 */
package com.rssfeednewsreader.repositories;

import com.rssfeednewsreader.entities.FeedResults;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedResultsRepository extends JpaRepository<FeedResults, Long>
{
    /**
     * Finds all entries based on the generated ID.
     *
     * @param uniqueId the generated ID
     * @return the list of results
     */
    List<FeedResults> findAllByFrequencyId(Long uniqueId);
}
