package com.leapwise.zganjer.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.leapwise.zganjer.model.RSSOutputData;

public final class OutputDataMapper implements RowMapper<RSSOutputData> {

	@Override
	public RSSOutputData mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		RSSOutputData rssOutputData = new RSSOutputData();
		rssOutputData.setKeyword(resultSet.getString("keyword"));
		rssOutputData.setOccurrences(resultSet.getInt("occurrences"));
		rssOutputData.setTitle(resultSet.getString("title"));
		rssOutputData.setNewsUrl(resultSet.getString("news_Url"));
		return rssOutputData;
	} 
}
