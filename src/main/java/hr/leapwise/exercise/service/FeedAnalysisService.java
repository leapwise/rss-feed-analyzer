package hr.leapwise.exercise.service;

import hr.leapwise.exercise.domain.entities.Item;

import java.util.List;

public interface FeedAnalysisService {

    Long analyseFeeds(String[] feedUrls);

    List<Item> getAnalysisResult(Long analysisResultId);
}
