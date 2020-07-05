package com.tp.rssffa.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class RSSFeedAnalyticsDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title;
	private String url;
}
