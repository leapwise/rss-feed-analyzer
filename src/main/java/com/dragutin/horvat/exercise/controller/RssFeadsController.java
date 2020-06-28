package com.dragutin.horvat.exercise.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dragutin.horvat.exercise.model.HotTopics;
import com.dragutin.horvat.exercise.model.dto.HotTopicDto;
import com.dragutin.horvat.exercise.service.IHotTopicAnalysisService;

@Controller
public class RssFeadsController {

	private IHotTopicAnalysisService iHotTopicAnalysisService;

	public RssFeadsController(IHotTopicAnalysisService pHotTopicAnalysisService) {
		this.iHotTopicAnalysisService = pHotTopicAnalysisService;
	}

	@PostMapping(value = "/analyse/new")
	@ResponseBody
	public ResponseEntity<String> analyseFeed(@RequestBody List<String> urls) {
		
		String uuid = UUID.randomUUID().toString();
		
		urls.forEach(url -> {
			
			iHotTopicAnalysisService.analyseFeed(url, uuid);
		});
		return new ResponseEntity<String>(uuid, HttpStatus.OK);

	}

	@GetMapping(value = "/frequency/{uuid}")
	@ResponseBody
	public List<HotTopicDto> getHotTopics(@PathVariable("uuid") String uuid) {
		return iHotTopicAnalysisService.fetchAnalysedNewsFeed(uuid);
	}

}
