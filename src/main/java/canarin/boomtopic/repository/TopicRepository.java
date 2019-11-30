package canarin.boomtopic.repository;

import canarin.boomtopic.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    /**
     * @param id
     * @return list of all topics
     */
    List<Topic> findAllByIdentifier(Long id);
}
