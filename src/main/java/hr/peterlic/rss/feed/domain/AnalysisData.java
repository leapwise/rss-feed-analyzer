package hr.peterlic.rss.feed.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represents data given from RSS feed analysis.
 * 
 * @author Ana Peterlic
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class AnalysisData implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "rssFeed_guid", nullable = false)
	private RssFeed rssFeed;

	/**
	 * Set that contains three most frequently occurred topics (nouns) inside the
	 * RSS feed request.
	 */
	@ElementCollection
	private Set<String> topics;

	@ElementCollection
	@OneToMany(mappedBy = "analysisData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<NewsArticleEntity> newsArticles;

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("AnalysisData{");
		sb.append("id=").append(id);
		sb.append(", rssFeed=").append(rssFeed);
		sb.append(", topics=").append(topics);
		sb.append('}');
		return sb.toString();
	}
}
