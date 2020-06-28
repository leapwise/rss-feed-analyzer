package com.dragutin.horvat.exercise.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
@IdClass(NewsFeedKey.class)
public class NewsFeed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String uuid;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(length=2048)
	private String url;
	@Column(length=2048)
	private String title;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "news_feed_uuid", referencedColumnName = "uuid")
	@JoinColumn(name = "news_feed_id", referencedColumnName = "id")
	private List<HotTopics> hotTopics;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<HotTopics> getHotTopics() {
		return hotTopics;
	}

	public void setHotTopics(List<HotTopics> hotTopics) {
		this.hotTopics = hotTopics;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hotTopics == null) ? 0 : hotTopics.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsFeed other = (NewsFeed) obj;
		if (hotTopics == null) {
			if (other.hotTopics != null)
				return false;
		} else if (!hotTopics.equals(other.hotTopics))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NewsFeed [uuid=" + uuid + ", id=" + id + ", url=" + url + ", title=" + title + ", hotTopics="
				+ hotTopics + "]";
	}
	
	
}
