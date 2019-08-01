/**
 * FeedResults is an entity class which represents the search results database
 *
 * @author  Ante Hakstok
 * @version 1.0
 * @since   2019-07-31
 */
package com.rssfeednewsreader.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity

public class FeedResults
{
    private @Id
    @GeneratedValue
    Long id;
    private Long frequencyId;
    private String keyword;
    private Integer wordCount;
    private String newsTitle;
    private String newsLink;

    /**
     * Instantiates the FeedResults class
     */
    public FeedResults()
    { }

    /**
     * Instantiates the FeedResults class
     *
     * @param frequencyId generated ID that represents that specific search operation
     * @param keyword     a keyword obtained from the news title
     * @param wordCount   amount of times the specific keyword appeared in the RSS Feeds
     * @param newsTitle   the title from which the keyword originated
     * @param newsLink    a link to the specific news article
     */
    public FeedResults(Long frequencyId, String keyword, Integer wordCount, String newsTitle, String newsLink)
    {
        this.frequencyId = frequencyId;
        this.keyword = keyword;
        this.newsTitle = newsTitle;
        this.newsLink = newsLink;
        this.wordCount = wordCount;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets frequency id.
     *
     * @return the frequency id
     */
    public Long getFrequencyId() {
        return frequencyId;
    }

    /**
     * Sets frequency id.
     *
     * @param frequencyId the frequency id
     */
    public void setFrequencyId(Long frequencyId) {
        this.frequencyId = frequencyId;
    }

    /**
     * Gets keyword.
     *
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Sets keyword.
     *
     * @param keyword the keyword
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Gets news link.
     *
     * @return the news link
     */
    public String getNewsLink() {
        return newsLink;
    }

    /**
     * Sets news link.
     *
     * @param newsLink the news link
     */
    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    /**
     * Gets news title.
     *
     * @return the news title
     */
    public String getNewsTitle() {
        return newsTitle;
    }

    /**
     * Sets news title.
     *
     * @param newsTitle the news title
     */
    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    /**
     * Gets word count.
     *
     * @return the word count
     */
    public Integer getWordCount() {
        return wordCount;
    }

    /**
     * Sets word count.
     *
     * @param wordCount the word count
     */
    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }
}
