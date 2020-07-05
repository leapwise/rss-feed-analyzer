package com.tp.rssffa.util;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tp.rssffa.model.RSSFeedAnalytics;

public class AnalyzeHelper {

	private AnalyzeHelper() {
	}

	public static List<String> searchForTopThreeWords(Map<String, Integer> listOfCountedWords) {
		return listOfCountedWords.entrySet()
				.stream()
				.sorted(Map.Entry
						.comparingByValue(Comparator.reverseOrder()))
				.limit(3).map(e -> e.getKey())
				.collect(toList());
	}

	public static ConcurrentMap<String, Integer> countWordsAndCreateMapOfThem(List<RSSFeedAnalytics> listOfRssFeeds) {
		return listOfRssFeeds.parallelStream()
				.flatMap(AnalyzeHelper.getFunctionToSplitWords())
				.collect(Collectors
						.toConcurrentMap(w -> w.toLowerCase(), w -> 1, Integer::sum));
	}
	
	public static List<RSSFeedAnalytics> getListOfThreeData(Map<String, List<RSSFeedAnalytics>> map) {
		List<RSSFeedAnalytics> listToReturn = new ArrayList<>();
		Integer order = 1;
		for (Map.Entry<String, List<RSSFeedAnalytics>> pair : map.entrySet()) {
			for (RSSFeedAnalytics item : pair.getValue()) {
				item.setApperance(order);
				order++;
				listToReturn.add(item);
				if (listToReturn.size() == 3)
					break;
			}
			if (listToReturn.size() == 3)
				break;
		}
		return listToReturn;
	}
	
	public static List<RSSFeedAnalytics> getValuesForMap(List<RSSFeedAnalytics> listOfRssFeeds, String topWord) {
		return listOfRssFeeds.stream()
				.filter(e -> e.getTitleWithoutPageName()
						.contains(topWord))
				.collect(toList());
	}

	public static Function<? super RSSFeedAnalytics, ? extends Stream<? extends String>> getFunctionToSplitWords() {
		return s -> asList(s.getTitleWithoutPageName().split(" ")).stream();
	}
}
