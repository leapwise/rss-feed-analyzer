package hr.peterlic.rss.feed.domain;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represents RSS feed entity.
 * 
 * @author Ana Peterlic
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class RssFeed implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String guid;

	@ElementCollection
	private List<String> urls;

	@Enumerated(EnumType.STRING)
	private RssFeedStatus status;

	/**
	 * When data is stored in database, automatically create GUID value and set
	 * status to IN_PROGRESS
	 */
	@PrePersist
	private void onCreate()
	{
		this.guid = UUID.randomUUID().toString();
		this.status = RssFeedStatus.IN_PROGRESS;
	}

	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder("RssFeed{");
		sb.append("id=").append(id);
		sb.append(", guid='").append(guid).append('\'');
		// sb.append(", urls=").append(urls);
		sb.append(", status=").append(status);
		sb.append('}');
		return sb.toString();
	}
}
