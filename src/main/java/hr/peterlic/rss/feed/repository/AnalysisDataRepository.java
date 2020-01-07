package hr.peterlic.rss.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.peterlic.rss.feed.domain.AnalysisData;

/**
 * @author Ana Peterlic
 */
@Repository
public interface AnalysisDataRepository extends JpaRepository<AnalysisData, Long>
{
	AnalysisData findOneByRssFeed_Guid(String guid);

}
