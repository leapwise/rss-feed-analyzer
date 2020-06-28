package com.dragutin.horvat.exercise.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dragutin.horvat.exercise.model.HotTopics;
import com.dragutin.horvat.exercise.model.dto.HotTopicDto;

public interface IHotTopicAnalysisService {

	List<HotTopicDto>  fetchAnalysedNewsFeed(String uuid);

	void analyseFeed(String url, String uuid);

}
