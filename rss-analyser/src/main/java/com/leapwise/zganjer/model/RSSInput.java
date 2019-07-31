package com.leapwise.zganjer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RSSInput {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String rss1;
	private String rss2;
	private String rss3;
	private String rss4;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRss1() {
		return rss1;
	}
	public void setRss1(String rss1) {
		this.rss1 = rss1;
	}
	public String getRss2() {
		return rss2;
	}
	public void setRss2(String rss2) {
		this.rss2 = rss2;
	}
	public String getRss3() {
		return rss3;
	}
	public void setRss3(String rss3) {
		this.rss3 = rss3;
	}
	public String getRss4() {
		return rss4;
	}
	public void setRss4(String rss4) {
		this.rss4 = rss4;
	}	
}
