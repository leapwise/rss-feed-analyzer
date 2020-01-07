package hr.peterlic.rss.feed.domain;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ana Peterlic
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "news_article")
public class NewsArticleEntity implements Serializable
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Lob
	@Column(length = 800)
	private String link;

	@ManyToOne
	@JoinColumn(name = "analysisData_id", nullable = false)
	private AnalysisData analysisData;

	private Integer ratings;

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("NewsArticle{");
		sb.append("id=").append(id);
		sb.append(", title='").append(title).append('\'');
		sb.append(", link='").append(link).append('\'');
		sb.append(", ratings=").append(ratings);
		sb.append('}');
		return sb.toString();
	}
}
