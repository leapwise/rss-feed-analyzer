package com.dragutin.horvat.exercise.service;

import static org.simmetrics.builders.StringMetricBuilder.with;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dragutin.horvat.exercise.model.HotTopics;
import com.dragutin.horvat.exercise.model.NewsFeed;
import com.dragutin.horvat.exercise.model.dto.HotTopicDto;
import com.dragutin.horvat.exercise.model.dto.NewsFeedDto;
import com.dragutin.horvat.exercise.repository.IHotTopicsAnalysisRepository;
import com.dragutin.horvat.exercise.repository.INewsFeedRepository;

@Service
public class HotTopicAnalysisServiceImpl implements IHotTopicAnalysisService {

	private static final Logger LOGGER = LoggerFactory.getLogger(HotTopicAnalysisServiceImpl.class);

	private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ROOT);
	private static final BigDecimal percentageOfSimilarity = new BigDecimal(0.40);
	private static final StringMetric algoritam = buildAlgoritam();

	private IHotTopicsAnalysisRepository iHotTopicsAnalysisRepository;
	private RssFeedParser iRssFeedAnalyserService;
	private INewsFeedRepository iNewsFeedRepository;

	public HotTopicAnalysisServiceImpl(INewsFeedRepository pNewsFeedRepository,
			IHotTopicsAnalysisRepository pHotTopicAnalysisRepository, RssFeedParser pRssFeedAnalyserService) {
		this.iNewsFeedRepository = pNewsFeedRepository;
		this.iRssFeedAnalyserService = pRssFeedAnalyserService;
		this.iHotTopicsAnalysisRepository = pHotTopicAnalysisRepository;
	}

	@Override
	public List<HotTopicDto> fetchAnalysedNewsFeed(String uuid) {
		List<HotTopicDto> listOfNewsfeedDtos = new ArrayList<>();
		Page<HotTopics> pageableTopics = iHotTopicsAnalysisRepository.findAllByUuid(uuid,
				PageRequest.of(0, 3));

		pageableTopics.getContent().forEach(hotTopics -> {
			
			List<NewsFeedDto> listofNews = new ArrayList<>();
			
			HotTopicDto hotTopic = new HotTopicDto();
			NewsFeedDto news = new NewsFeedDto();
			
			news.setTitle(hotTopics.getNewsFeed().getTitle());
			news.setUrl(hotTopics.getNewsFeed().getUrl());
			
			listofNews.add(news);
			
			hotTopic.setNewsFeedDto(listofNews);
			listOfNewsfeedDtos.add(hotTopic);
			});

		return listOfNewsfeedDtos;
	}

	private void checkNewsOcurances(NewsFeed newsToCheck, List<NewsFeed> listOfRssUrlNewsFeed, String uuid) {
		List<HotTopics> listOfHotTopics = new ArrayList<>();

		for (NewsFeed news : listOfRssUrlNewsFeed) {

			if (news.equals(newsToCheck))
				continue;

			BigDecimal value = BigDecimal.valueOf(algoritam.compare(newsToCheck.getTitle(), news.getTitle()));

			LOGGER.info("Comparing titles similarity {} %	 {} - {} ",
					numberFormat.format(value.multiply(new BigDecimal(100))), newsToCheck.getTitle(), news.getTitle());

			if (value.compareTo(percentageOfSimilarity) == 1) {
				LOGGER.info("This titles are similar {} %", numberFormat.format(value.multiply(new BigDecimal(100))));
				HotTopics topic = new HotTopics();
				topic.setName(newsToCheck.getTitle());
				listOfHotTopics.add(topic);
				LOGGER.info("Similar news, occurrences {} - {}", newsToCheck.getTitle(), news.getTitle());
			}

		}
		newsToCheck.setUuid(uuid);
		newsToCheck.setHotTopics(listOfHotTopics);
	}

	private static StringMetric buildAlgoritam() {
		return with(new CosineSimilarity<String>()).simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
				.simplify(Simplifiers.replaceNonWord()).tokenize(Tokenizers.whitespace()).build();
	}

	@Override
	@Transactional
	@Async
	public void analyseFeed(String url, String uuid) {

		// For each rss url we are parsing news and saving to map
		List<NewsFeed> listOfRssUrlNewsFeed = iRssFeedAnalyserService.parseRssFeedUrl(url);
		listOfRssUrlNewsFeed.parallelStream().forEach(news -> checkNewsOcurances(news, listOfRssUrlNewsFeed, uuid));

		// Saving to db and returning id
		listOfRssUrlNewsFeed.stream()
//			.distinct()
				.filter(newsFeed -> !newsFeed.getHotTopics().isEmpty()).forEach(iNewsFeedRepository::save);

	}

}
