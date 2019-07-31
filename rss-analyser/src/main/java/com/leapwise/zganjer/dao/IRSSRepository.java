package com.leapwise.zganjer.dao;

import java.util.List;

import com.leapwise.zganjer.model.RSSOutputData;

public interface IRSSRepository {
	public Number insertRSSInput(List<String> inputFeeds);
	public Number insertRSSFeedTitle(String inputId, String title, String newsUrl);
	public Number insertRssTitleKeyword(String feedTitleId, String keyword);
	public Number insertRssAnalysis(String inputId, String keyword, Integer occurrences);
	public List<RSSOutputData> getAnalyzedData(Long inputId);
}
