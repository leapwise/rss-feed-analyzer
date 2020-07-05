package com.tp.rssffa.model;

import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.rometools.rome.feed.synd.SyndEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RSSFeedAnalytics {

	private static final String DELIMITOR = " -";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String uuid;
	@Column( length = 1000 )
	private String title;
	@Column( length = 1000 )
	private String url;
	private int apperance;

	public static RSSFeedAnalytics getRSSFeedAnalyticsFromSyndEntry(SyndEntry syndEntry) {
		return new RSSFeedAnalytics(syndEntry.getTitle(), syndEntry.getLink());
	}

	private RSSFeedAnalytics(String title, String url) {
		this.title = title;
		this.url = url;
	}

	public String getTitleWithoutPageName() { 
		return substringBeforeLast(this.title, DELIMITOR).toLowerCase();
	}

	@Override
	public String toString() {
		return "RSSFeedAnalytics [title=" + title + ", url=" + url + ", apperance=" + apperance + "]";
	}
	
	
}
