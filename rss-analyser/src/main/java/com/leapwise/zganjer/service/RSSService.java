package com.leapwise.zganjer.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leapwise.zganjer.dao.IRSSRepository;
import com.leapwise.zganjer.helper.WordTypes;
import com.leapwise.zganjer.singleton.Pipeline;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;

@Service
public class RSSService {

	@Autowired
	IRSSRepository rssRepository;
	
	private String finalString;
	
	public String processRSSFeed(List<String> inputFeeds) {
		finalString = "";
		String result = "";
		try {
			result = rssRepository.insertRSSInput(inputFeeds).toString();
			readWriteRSSFeed(result, inputFeeds);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public void readWriteRSSFeed(String rssInputId, List<String> inputFeeds) throws MalformedURLException, IOException, IllegalArgumentException, FeedException {
		String result = "";
		for (String input : inputFeeds) {
			if (input != null) {
				try {
					XmlReader reader = new XmlReader(new URL(input));
					SyndFeed feed = new SyndFeedInput().build(reader);
					for (SyndEntry entry : (List<SyndEntry>) feed.getEntries()) {
						result = rssRepository.insertRSSFeedTitle(rssInputId, entry.getTitle(), entry.getLink()).toString();
						parseFeedTitle(result, entry.getTitle());
					}			
				} catch (MalformedURLException mUrlException) {
					System.out.println("Pogreška kod čitanja URLa");
				}				
			}
		}	
		analyseRSSKeywords(finalString, rssInputId);
	}
	
	public void parseFeedTitle(String feedTitleId, String title) {
		StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
		CoreDocument coreDocument = new CoreDocument(title);
		stanfordCoreNLP.annotate(coreDocument);
		List<CoreLabel> coreLabels = coreDocument.tokens();
		for(CoreLabel coreLabel : coreLabels) {
			if (EnumUtils.isValidEnum(WordTypes.class, coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class))) {
				rssRepository.insertRssTitleKeyword(feedTitleId, coreLabel.originalText()).toString();
				finalString += coreLabel.originalText() + " ";
			}
		}		
	}
	
	public void analyseRSSKeywords(String checkString, String rssInputId) {
		Map<String, Integer> words = new HashMap<String, Integer>();
		for (String word : checkString.split(" ")) {
			Integer count = words.get(word);
			if (count != null) 
				count++;
			else
				count = 1;
			words.put(word, count);
		}
		for (Map.Entry<String, Integer> entry : words.entrySet()) {
			rssRepository.insertRssAnalysis(rssInputId, entry.getKey(), entry.getValue());
		}
	}
}
