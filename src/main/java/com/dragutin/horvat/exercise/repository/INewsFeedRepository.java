package com.dragutin.horvat.exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dragutin.horvat.exercise.model.NewsFeed;

public interface INewsFeedRepository extends JpaRepository<NewsFeed, Long> {

}
