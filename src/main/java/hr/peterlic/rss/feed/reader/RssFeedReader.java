package hr.peterlic.rss.feed.reader;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ana Peterlic
 */
@Slf4j
@Component
public class RssFeedReader
{

	public List<SyndEntry> getSyndFeedEntriesFromUrl(String url) throws IOException, FeedException
	{
		URL feedSource = new URL(url);
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(feedSource));
		return feed.getEntries();
	}
}
