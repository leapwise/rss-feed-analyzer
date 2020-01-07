package hr.peterlic.rss.feed.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import hr.peterlic.rss.feed.api.request.AnalyseRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class RssFeedAnalysisControllerTest
{
	@Autowired
	private MockMvc mockMvc;

	@Test
	void testWhenPost_thenReturn200() throws Exception
	{
		log.info("\n\n <3 testWhenGetDashboard_thenReturn200");

		List<String> urls = new ArrayList<>();
		urls.add("https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss");
		urls.add("https://news.google.com/rss/search?cf=all&hl=en-US&pz=1&q=astronomy&gl=US&ceid=US:en");

		String requestJson = analysisRequestToJson(AnalyseRequest.builder().urls(urls).build());

		this.mockMvc.perform(post("/analyse/new").contentType(APPLICATION_JSON).content(requestJson)).andExpect(status().isOk());

	}

	@Test
	void testWhenPost_thenReturn404() throws Exception
	{
		log.info("\n\n <3 testWhenGetDashboard_thenReturn200");

		String requestJson = analysisRequestToJson(AnalyseRequest.builder().urls(List.of("vdsvas")).build());

		MvcResult result = this.mockMvc.perform(post("/analyse/new").contentType(APPLICATION_JSON).content(requestJson))
				.andExpect(status().isBadRequest()).andReturn();

		log.info("Result: {}", result);
	}

	@Test
	void testWhenPostWithNull_thenReturn404() throws Exception
	{
		log.info("\n\n <3 testWhenGetDashboard_thenReturn200");

		String requestJson = analysisRequestToJson(AnalyseRequest.builder().urls(null).build());

		this.mockMvc.perform(post("/analyse/new").contentType(APPLICATION_JSON).content(requestJson)).andExpect(status().isBadRequest());
	}

	private String analysisRequestToJson(AnalyseRequest analyseRequest) throws JsonProcessingException
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(analyseRequest);
	}

}
