package canarin.boomtopic.service;

import canarin.boomtopic.domain.Topic;
import canarin.boomtopic.repository.TopicRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    /**
     * This is used for saving topic
     *
     * @param topic
     */
    public void saveTopic(Topic topic) {
        topicRepository.save(topic);
    }

    /**
     * @param id
     * @return all topics with provided id
     */
    public ResponseEntity<List<Topic>> getTopicsByIdentifier(Long id) {
        return ResponseEntity.ok(topicRepository.findAllByIdentifier(id));
    }
}
