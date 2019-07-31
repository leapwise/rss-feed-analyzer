package com.leapwise.zganjer.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.leapwise.zganjer.dao.IRSSRepository;
import com.leapwise.zganjer.model.RSSOutputData;

@RestController
@RequestMapping("/frequency/")
public class RSSFetchController {

	@Autowired
	IRSSRepository rssRepository;
	
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)	
	public List<RSSOutputData> getAnalyzedData(@PathVariable Long id) {
		return rssRepository.getAnalyzedData(id);
	}
}
