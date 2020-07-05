package com.tp.rssffa.util;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tp.rssffa.constants.MessagesConstants;
import com.tp.rssffa.exception.RSSFeedException;
import com.tp.rssffa.model.RSSFeedAnalytics;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

public class AnalyzeHelper {

	private static final String FILE = "en-pos-maxent.bin";
	private static final String APLHANUMERIC = "[^a-zA-Z0-9\\s]";

	private AnalyzeHelper() {
	}

	public static List<String> searchForTopThreeWords(Map<String, Integer> listOfCountedWords) {
		return listOfCountedWords.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(3).map(e -> e.getKey()).collect(toList());
	}

	public static Map<String, Integer> countWordsAndCreateMapOfThem(List<RSSFeedAnalytics> listOfRssFeeds) {
		List<String> listOfTaggedWords = listOfRssFeeds.parallelStream()
				.map(data -> retrievePartOfSpeech(data.getTitleWithoutPageName())).flatMap(x -> x.stream())
				.collect(toList());
		return listOfTaggedWords.parallelStream()
				.collect(Collectors.toConcurrentMap(w -> w, w -> 1, Integer::sum));
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
		return listOfRssFeeds.stream().filter(e -> e.getTitleWithoutPageName().contains(topWord)).collect(toList());
	}

	public static Function<? super RSSFeedAnalytics, ? extends Stream<? extends String>> getFunctionToSplitWords() {
		return s -> asList(s.getTitleWithoutPageName().split(" ")).stream();
	}

	
	/**
	 * OpenNLP library 
	 * Through this method we are retrieving only words that are important(i.e. nouns)
	 * We are removing from title all non alphanumeric words and separating title into array of string.
	 * Then we tagging all words through library of english language and retrieving only those words 
	 * whose been tagged with "NNP", "NN", "NNS" or "NNPS" tag.
	 * 
	 * https://www.tutorialspoint.com/opennlp/index.htm
	 * @param title
	 * @return
	 */
	private static List<String> retrievePartOfSpeech(String title) {
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
		String[] tokens = tokenizer.tokenize(title.replaceAll(APLHANUMERIC, ""));
		POSModel posModel = getPosModel();
		POSTaggerME posTagger = new POSTaggerME(posModel);
		String[] tags = posTagger.tag(tokens);
		
		return filterWordsOnly(tokens, tags);
	}

	private static POSModel getPosModel() {
		try {
			ClassLoader classLoader = AnalyzeHelper.class.getClassLoader();
			InputStream inputStreamPOSTagger = classLoader.getResourceAsStream(FILE);
			return new POSModel(inputStreamPOSTagger);
		} catch (IOException e) {
			throw new RSSFeedException(MessagesConstants.ERROR_WHILE_CREATING_TOKENS);
		}
	}

	private static List<String> filterWordsOnly(String[] tokens, String[] tags) {
		List<String> words = new ArrayList<>();
		for (int i = 0; i < tags.length; i++) {
			if ("NNP".equals(tags[i]) || "NN".equals(tags[i]) || "NNS".equals(tags[i]) || "NNPS".equals(tags[i])) {
				words.add(tokens[i].toLowerCase());
			}
		}
		return words;
	}
}
