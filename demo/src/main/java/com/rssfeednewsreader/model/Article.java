/**
 * Article is a model class that represents the search results
 *
 * @author  Ante Hakstok
 * @version 1.0
 * @since   2019-07-31
 */
package com.rssfeednewsreader.model;

import java.net.URL;
import java.util.List;

public class Article
{
    private String title;
    private String link;
    private List<String> searchableWords;
    private URL feedURL;

    /**
     * Instantiates the Article class
     *
     * @param title           the news title
     * @param link            the news link
     * @param searchableWords a list of searchable keywords derived from the titles
     * @param feedURL         url of a feed
     */
    public Article(String title, String link, List<String> searchableWords, URL feedURL)
    {
        this.title = title;
        this.link = link;
        this.searchableWords = searchableWords;
        this.feedURL = feedURL;
    }

    /**
     * Gets feed url.
     *
     * @return the feed url
     */
    public URL getFeedURL()
    {
        return feedURL;
    }

    /**
     * Sets feed url.
     *
     * @param feedURL the feed url
     */
    public void setFeedURL(URL feedURL)
    {
        this.feedURL = feedURL;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets link.
     *
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets link.
     *
     * @param link the link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Gets searchable words.
     *
     * @return the searchable words
     */
    public List<String> getSearchableWords() {
        return searchableWords;
    }

    /**
     * Sets searchable words.
     *
     * @param searchableWords the searchable words
     */
    public void setSearchableWords(List<String> searchableWords) {
        this.searchableWords = searchableWords;
    }
}
