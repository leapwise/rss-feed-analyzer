package hr.peterlic.rss.feed.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.peterlic.rss.feed.service.RssFeedService;

/**
 * @author Ana Peterlic
 */
@RestController
@RequestMapping("/frequency")
public class RssFeedFrequencyController
{
	private RssFeedService rssFeedService;

	@Autowired
	public RssFeedFrequencyController(RssFeedService rssFeedService)
	{
		this.rssFeedService = rssFeedService;
	}

	/**
	 * Method that retrieves three hot topics with feed titles and links.<br>
	 * GET /frequency/{id}
	 * 
	 * @param guid
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity getFrequencyInfo(@NotNull @PathVariable("id") String guid)
	{
		return ResponseEntity.ok(rssFeedService.fetchHotTopicRssFeedItems(guid));
	}
}
