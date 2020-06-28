package com.dragutin.horvat.exercise.model.dto;

import java.util.List;

public class HotTopicDto {

	private List<NewsFeedDto> newsFeedDto;

	public List<NewsFeedDto> getNewsFeedDto() {
		return newsFeedDto;
	}

	public void setNewsFeedDto(List<NewsFeedDto> newsFeedDto) {
		this.newsFeedDto = newsFeedDto;
	}

	@Override
	public String toString() {
		return "HotTopicDto [newsFeedDto=" + newsFeedDto + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((newsFeedDto == null) ? 0 : newsFeedDto.hashCode());
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
		HotTopicDto other = (HotTopicDto) obj;
		if (newsFeedDto == null) {
			if (other.newsFeedDto != null)
				return false;
		} else if (!newsFeedDto.equals(other.newsFeedDto))
			return false;
		return true;
	}

}
