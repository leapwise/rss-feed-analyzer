package com.leapwise.zganjer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.leapwise.zganjer.model.RSSOutputData;

@Repository
public class RSSRepository implements IRSSRepository{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public Number insertRSSInput(final List<String> inputFeeds) {
		final String sqlQuerry = "insert into RSSInput (rss1, rss2, rss3, rss4) values (?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqlQuerry,new String[] {"id"});
				ps.setString(1, inputFeeds.get(0));
				ps.setString(2, inputFeeds.get(1));
				ps.setString(3, inputFeeds.get(2));
				ps.setString(4, inputFeeds.get(3));
				return ps;
			}
		},keyHolder);												
		return keyHolder.getKey();
	}

	@Override
	public Number insertRSSFeedTitle(final String inputId, final String title, final String newsUrl) {
		final String sqlQuerry = "insert into RSSFeed_Title (input_Id, title, news_Url) values (?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqlQuerry,new String[] {"id"});
				ps.setString(1, inputId);
				ps.setString(2, title);
				ps.setString(3, newsUrl);
				return ps;
			}
		},keyHolder);												
		return keyHolder.getKey();
	}
	
	@Override
	public Number insertRssTitleKeyword(final String feedTitleId, final String keyword) {
		final String sqlQuerry = "insert into RSSTitle_Keyword (feed_Title_id, keyword) values (?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqlQuerry,new String[] {"id"});
				ps.setString(1, feedTitleId);
				ps.setString(2, keyword);				
				return ps;
			}
		},keyHolder);												
		return keyHolder.getKey();
	}
	
	@Override
	public Number insertRssAnalysis(final String inputId, final String keyword, final Integer occurrences) {
		final String sqlQuerry = "insert into RSSAnalysis (input_id, keyword, occurrences) values (?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqlQuerry,new String[] {"id"});
				ps.setString(1, inputId);
				ps.setString(2, keyword);
				ps.setLong(3, occurrences);				
				return ps;
			}
		},keyHolder);												
		return keyHolder.getKey();
	}
	
	public List<RSSOutputData> getAnalyzedData(final Long inputId) {
		final String sqlQuerry = "select rsa.keyword, rsa.occurrences, rft.title, rft.news_url from rssanalysis rsa, rsstitle_keyword rtk, rssfeed_title rft, rssinput rsi\r\n" + 
				"where \r\n" + 
				"rsi.id = (?) and\r\n" + 
				"rsa.input_id = rsi.id and\r\n" + 
				"rsa.keyword IN (select top (3) rsk.keyword from rssanalysis rsk where rsk.input_id = (?) order by occurrences desc) and\r\n" + 
				"rtk.keyword = rsa.keyword and\r\n" + 
				"rtk.feed_title_id = rft.id and\r\n" + 
				"rft.input_id = rsi.id\r\n" + 
				"order by rsa.occurrences desc, rsa.keyword";
		return jdbcTemplate.query(sqlQuerry, new Object[] {inputId,inputId}, new OutputDataMapper());
	}
}
