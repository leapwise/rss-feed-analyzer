package hr.peterlic.rss.feed.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import hr.peterlic.rss.feed.domain.RssFeed;
import hr.peterlic.rss.feed.domain.RssFeedStatus;
import hr.peterlic.rss.feed.repository.RssFeedRepository;
import hr.peterlic.rss.feed.util.ConstantsUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class RssFeedFrequencyControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RssFeedRepository rssFeedRepository;

	@Transactional
	@Test
	void testWhenGetFrequency_thenReturn404_notFound() throws Exception
	{
		log.info("testWhenGetFrequency_thenReturn404_notFound");

		MvcResult result = this.mockMvc.perform(get("/frequency/{id}", "25665-548789")).andExpect(status().isBadRequest()).andReturn();

		// status -> not found
		Assertions.assertTrue(result.getResponse().getContentAsString().contains(ConstantsUtils.ERROR_RSS_FEED_GUID_DOES_NOT_EXIST));
	}

	@Test
	void testWhenGetFrequency_thenReturn404_statusInProgress() throws Exception
	{
		log.info("testWhenGetFrequency_thenReturn404_statusInProgress");

		List<String> urls = new ArrayList<>();
		urls.add("https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss");
		urls.add("https://news.google.com/rss/search?cf=all&hl=en-US&pz=1&q=astronomy&gl=US&ceid=US:en");

		RssFeed rssFeedStoredElement = rssFeedRepository.save(RssFeed.builder().urls(urls).build());

		Assertions.assertNotNull(rssFeedStoredElement);
		Assertions.assertNotNull(rssFeedStoredElement.getId());

		log.info("Rss feed stored element: {}", rssFeedStoredElement);

		MvcResult result = this.mockMvc.perform(get("/frequency/{id}", rssFeedStoredElement.getGuid())).andExpect(status().isNotFound())
				.andReturn();

		// status -> IN_PROGRESS
		Assertions.assertTrue(result.getResponse().getContentAsString().contains(ConstantsUtils.ERROR_RSS_FEED_STATUS_IN_PROGRESS));
	}

	@Test
	void testWhenGetFrequency_thenReturn404_statusFailed() throws Exception
	{
		log.info("testWhenGetFrequency_thenReturn404_statusFailed");

		List<String> urls = new ArrayList<>();
		urls.add("https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss");
		urls.add("https://news.google.com/rss/search?cf=all&hl=en-US&pz=1&q=astronomy&gl=US&ceid=US:en");

		RssFeed rssFeedStoredElement = rssFeedRepository.save(RssFeed.builder().urls(urls).build());

		Assertions.assertNotNull(rssFeedStoredElement);
		Assertions.assertNotNull(rssFeedStoredElement.getId());

		rssFeedStoredElement.setStatus(RssFeedStatus.FAILED);
		rssFeedRepository.save(rssFeedStoredElement);

		log.info("Rss feed stored element: {}", rssFeedStoredElement);

		MvcResult result = this.mockMvc.perform(get("/frequency/{id}", rssFeedStoredElement.getGuid())).andExpect(status().isNotFound())
				.andReturn();

		// status -> IN_PROGRESS
		Assertions.assertTrue(result.getResponse().getContentAsString().contains(ConstantsUtils.ERROR_RSS_STATUS_FEED_FAILED));
	}

}
