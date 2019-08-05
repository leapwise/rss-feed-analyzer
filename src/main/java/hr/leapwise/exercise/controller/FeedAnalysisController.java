package hr.leapwise.exercise.controller;

import hr.leapwise.exercise.domain.entities.Item;
import hr.leapwise.exercise.service.FeedAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FeedAnalysisController {

    @Autowired
    private FeedAnalysisService feedAnalysisService;

    @PostMapping("/analyse/new")
    private Long analyse(@RequestParam(value="feedUrls[]") String[] feedUrls) {
        return feedAnalysisService.analyseFeeds(feedUrls);
    }

    @GetMapping(path = "/frequency/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<ItemDto> getViralItems(@PathVariable("id") Long analysisId) {
        final List<Item> items = feedAnalysisService.getAnalysisResult(analysisId);

        // we won't use mapper since those are small objects (and are only meant ot be read form the outside - that's why DTO is used in the first place)
        // ->  so this kind of mapping is acceptable
        // Also, it's strictly said to return JASOn, so ResponseEntity is not used
        // Also, exceptions are not pretty handled
        return items.stream().map(ItemDto::new).collect(Collectors.toList());
    }



}
