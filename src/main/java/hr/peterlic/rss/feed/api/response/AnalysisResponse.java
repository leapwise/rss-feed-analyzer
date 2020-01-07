package hr.peterlic.rss.feed.api.response;

import lombok.Builder;
import lombok.Data;

/**
 * Class that represents response of hot topic analysis. It returns unique
 * identifier from {@link hr.peterlic.rss.feed.domain.RssFeed} class.
 */
@Builder
@Data
public class AnalysisResponse
{

	private String guid;
}
