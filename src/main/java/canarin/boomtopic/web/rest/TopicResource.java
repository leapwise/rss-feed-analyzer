package canarin.boomtopic.web.rest;

import canarin.boomtopic.domain.Topic;
import canarin.boomtopic.service.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TopicResource {

    private final TopicService topicService;

    public TopicResource(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * @param id
     * @return list of hot topics
     */
    @GetMapping("/frequency/{id}")
    @ResponseBody
    public ResponseEntity<List<Topic>> getHotTopics(@PathVariable Long id) {
        return topicService.getTopicsByIdentifier(id);
    }
}
