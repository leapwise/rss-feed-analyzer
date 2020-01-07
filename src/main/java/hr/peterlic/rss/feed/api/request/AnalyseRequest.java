package hr.peterlic.rss.feed.api.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ana Peterlic
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnalyseRequest implements Serializable
{
	/**
	 * List of URLs. Minimal two URLs have to be provided.
	 */
	@Size(min = 2)
	private List<String> urls;
}
