package hr.peterlic.rss.feed.api.response;

import java.util.List;

import hr.peterlic.rss.feed.api.model.NewsArticle;
import lombok.Data;

@Data
public class FrequencyResponse
{
	List<NewsArticle> newsArticleList;
}
