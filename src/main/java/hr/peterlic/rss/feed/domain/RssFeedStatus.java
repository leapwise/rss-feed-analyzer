package hr.peterlic.rss.feed.domain;

/**
 * Enumeration that represent RSS feed status. <br>
 * 
 * SUCCESSFUL - hot topic analysis is done and successful <br>
 * FAILED - hot topic analysis is done but unsuccessful <br>
 * IN_PROGRESS - hot topic analysis is still in progress
 * 
 * @author Ana Peterlic
 */
public enum RssFeedStatus
{
	SUCCESSFUL, IN_PROGRESS, FAILED;
}
