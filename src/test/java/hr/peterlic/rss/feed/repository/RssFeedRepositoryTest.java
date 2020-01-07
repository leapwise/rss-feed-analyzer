package hr.peterlic.rss.feed.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hr.peterlic.rss.feed.domain.RssFeed;
import hr.peterlic.rss.feed.domain.RssFeedStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class RssFeedRepositoryTest
{
	@Autowired
	private RssFeedRepository rssFeedRepository;

	@Test
	void testSaveNewRssFeed_Ok()
	{
		log.info("testSaveNewRssFeed_Ok");

		List<String> urls = new ArrayList<>();
		urls.add("https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss");
		urls.add("https://news.google.com/rss/search?cf=all&hl=en-US&pz=1&q=astronomy&gl=US&ceid=US:en");

		RssFeed rssFeedStoredElement = rssFeedRepository.save(RssFeed.builder().urls(urls).build());

		Assertions.assertNotNull(rssFeedStoredElement.getId());
		Assertions.assertNotNull(rssFeedStoredElement.getGuid());
		Assertions.assertEquals(rssFeedStoredElement.getStatus(), RssFeedStatus.IN_PROGRESS);
	}
}
