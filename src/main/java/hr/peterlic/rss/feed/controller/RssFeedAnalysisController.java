package hr.peterlic.rss.feed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.peterlic.rss.feed.api.request.AnalyseRequest;
import hr.peterlic.rss.feed.api.response.AnalysisResponse;
import hr.peterlic.rss.feed.exception.BadRequestException;
import hr.peterlic.rss.feed.service.RssFeedService;
import hr.peterlic.rss.feed.util.ConstantsUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Ana Peterlic
 */
@Slf4j
@RestController
@RequestMapping("/analyse")
public class RssFeedAnalysisController
{
	private RssFeedService rssFeedService;

	@Autowired
	public RssFeedAnalysisController(RssFeedService rssFeedService)
	{
		this.rssFeedService = rssFeedService;
	}

	@PostMapping("/new")
	public ResponseEntity getNewAnalysis(@Validated @RequestBody AnalyseRequest request, BindingResult result)
	{

		if (result.hasErrors())
		{
			log.error("Request has errors, action failed - ", result.getAllErrors());
			throw new BadRequestException(ConstantsUtils.ERROR_PROVIDED_REQUEST_IS_INCORRECT);
		}

		String guid = rssFeedService.returnUniqueIdentifier(request);

		new Thread(() -> rssFeedService.analyseRssFeeds(request, guid)).start();

		return ResponseEntity.ok(AnalysisResponse.builder().guid(guid).build());
	}
}
