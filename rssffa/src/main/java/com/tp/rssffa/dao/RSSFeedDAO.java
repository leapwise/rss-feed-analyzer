package com.tp.rssffa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tp.rssffa.model.RSSFeedAnalytics;

public interface RSSFeedDAO extends JpaRepository<RSSFeedAnalytics, Integer> {

	List<RSSFeedAnalytics> findByUuid(String uuid);
}
