package hr.peterlic.rss.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.peterlic.rss.feed.domain.RssFeed;

/**
 * @author Ana Peterlic
 */
@Repository
public interface RssFeedRepository extends JpaRepository<RssFeed, Long>
{
	Optional<RssFeed> findByGuid(String guid);

}
