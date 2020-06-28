package com.dragutin.horvat.exercise.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dragutin.horvat.exercise.model.HotTopics;

public interface IHotTopicsAnalysisRepository extends JpaRepository<HotTopics, Long> {
	
	@Query("SELECT t "
			+ "FROM HotTopics t " 
			+ "WHERE t.newsFeed.uuid = :uuid "
			+ "ORDER BY t.name ASC ")
//			+ "GROUP BY t.name "
//			+ "ORDER BY COUNT(t) DESC, t.name ASC")
	Page<HotTopics> findAllByUuid(@Param("uuid")String uuid, Pageable pageable);
	
}
