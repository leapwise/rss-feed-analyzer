package org.lsedlanic.exercise.rssfeed.service;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.lsedlanic.exercise.rssfeed.FunctionWithException;
import org.lsedlanic.exercise.rssfeed.models.AnalysisResult;
import org.lsedlanic.exercise.rssfeed.repository.AnalysisResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.net.URL;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnalysisService {
    @Autowired
    private AnalysisResultRepository repository;

    private Set<String> stopWords;


    public AnalysisService() {
        try {
            stopWords = new HashSet<>(Files.readAllLines(Paths.get("src/main/java/org/lsedlanic/exercise/rssfeed/service/stopwords.txt"), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Failed to load stop words: " + e.getMessage());
        }
    }

    public SyndFeed fetchAndParseFeed(String feedUrl) throws Exception {
        URL feedSource = new URL(feedUrl);
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(feedSource));
    }

    public List<String> tokenize(String text) {
        return Arrays.stream(text.split("\\s+"))
                .filter(word -> !stopWords.contains(word.toLowerCase()))
                .filter(word -> word.matches("\\p{Alpha}+"))
                .collect(Collectors.toList());
    }


    public UUID analyzeFeeds(List<String> feedUrls) {
        UUID analysisId = generateAnalysisId();

        List<SyndFeed> feeds = feedUrls.stream()
                .map(wrapper(this::fetchAndParseFeed))
                .collect(Collectors.toList());

        Map<String, Integer> tokenCounts = analyzeFeedsForOverlaps(feeds);

        List<String> hotTopics = getTopHotTopics(tokenCounts, 3);

        List<AnalysisResult> results = new ArrayList<>();
        for (String topic : hotTopics) {
            List<AnalysisResult> resultsForTopic = feeds.stream()
                    .flatMap(feed -> feed.getEntries().stream())
                    .filter(entry -> tokenize(entry.getDescription().getValue()).contains(topic))
                    .map(entry -> new AnalysisResult(topic, tokenCounts.get(topic), analysisId, entry.getTitle(), entry.getLink()))
                    .collect(Collectors.toList());

            AnalysisResult topResultForTopic = resultsForTopic.stream()
                    .sorted(Comparator.comparingInt(AnalysisResult::getFrequency).reversed())
                    .findFirst()
                    .orElse(null);

            if (topResultForTopic != null) {
                results.add(topResultForTopic);
            }
        }

        storeAnalysisResults(results);

        return analysisId;
    }

    private Map<String, Integer> analyzeFeedsForOverlaps(List<SyndFeed> feeds) {
        Map<String, Integer> tokenCounts = new HashMap<>();
        Set<String> allTokens = new HashSet<>();

        for (SyndFeed feed : feeds) {
            List<String> tokens = feed.getEntries().stream()
                    .map(entry -> tokenize(entry.getDescription().getValue()))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            allTokens.addAll(tokens);
        }

        for (String token : allTokens) {
            int count = (int) feeds.stream()
                    .flatMap(feed -> feed.getEntries().stream())
                    .filter(entry -> tokenize(entry.getDescription().getValue()).contains(token))
                    .count();
            tokenCounts.put(token, count);
        }

        return tokenCounts;
    }


    private UUID generateAnalysisId() {
        return UUID.randomUUID();
    }

    public List<String> getTopHotTopics(Map<String, Integer> tokenCounts, int topN) {
        return tokenCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void storeAnalysisResults(List<AnalysisResult> results) {
        repository.saveAll(results);
    }



    public List<AnalysisResult> getTopResults(UUID id) {
        List<AnalysisResult> allResults = repository.findByAnalysisId(id);

        return allResults.stream()
                .sorted((result1, result2) -> result2.getFrequency() - result1.getFrequency())
                .limit(3)
                .collect(Collectors.toList());
    }
    private <T, R, E extends Exception> Function<T, R> wrapper(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
