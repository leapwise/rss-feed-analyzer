package canarin.boomtopic.web.rest;

import canarin.boomtopic.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedResource {

    private final FeedService feedService;

    /**
     * Constructor
     * @param feedService
     */
    public FeedResource(FeedService feedService) {
        this.feedService = feedService;
    }

    /**
     * Performs fetch and search of the requested RSS Feeds
     *
     * @param feeds the feeds
     * @return the ID generated for the current search results
     */
    @GetMapping("/analyse/new")
    @ResponseBody
    ResponseEntity<String> fetchFeeds(@RequestParam("rss-feed") List<String> feeds) throws Exception {

        // We need at least 2 feeds otherwise we return exception.
        if (feeds.size() < 2) {
            return ResponseEntity.badRequest().body("Please enter at least 2 feeds.") ;
        }
        return feedService.fetchFeeds(feeds);
    }
}
