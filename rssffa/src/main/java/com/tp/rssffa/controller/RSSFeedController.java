package com.tp.rssffa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tp.rssffa.constants.MessagesConstants;
import com.tp.rssffa.exception.RSSFeedException;
import com.tp.rssffa.model.RSSFeedAnalyticsDto;
import com.tp.rssffa.service.RSSFeedAnalyticsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RSSFeedController {

	@Autowired
	private RSSFeedAnalyticsService rSSFeedAnalyticsService;

	@PostMapping("/analyse/new")
	public @ResponseBody ResponseEntity<String> analyseFeed(@RequestBody List<String> urls) {
		try {
			String uuid = rSSFeedAnalyticsService.analyse(urls);
			return new ResponseEntity<String>(uuid, HttpStatus.OK);
		} catch (RSSFeedException e) {
			log.info(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error(MessagesConstants.ERROR);
			log.error(e.getMessage());
			return new ResponseEntity<String>(MessagesConstants.ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/frequency/{id}")
	public @ResponseBody ResponseEntity<Object> getData(@PathVariable("id") String id) {
		try {
			List<RSSFeedAnalyticsDto> listOfData = rSSFeedAnalyticsService.getDataForUuid(id);
			return ResponseEntity.status(HttpStatus.OK).body(listOfData);
		} catch (RSSFeedException e) {
			log.info(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e) {
			log.error(MessagesConstants.ERROR);
			log.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessagesConstants.ERROR);
		}
	}
}
