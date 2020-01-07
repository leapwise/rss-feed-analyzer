package hr.peterlic.rss.feed.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represents one hot topic news article (RSS feed item).
 * 
 * @author Ana Peterlic
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewsArticle
{

	private String title;

	private String link;

	@JsonIgnore
	private Integer ratings;
}
