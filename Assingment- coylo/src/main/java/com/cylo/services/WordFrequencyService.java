package com.cylo.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cylo.beans.WordFrequency;
import com.cylo.beans.WordFrequencyStats;
import com.cylo.exceptions.ErrorWhileProcessingRequest;
import com.cylo.exceptions.WordFrequencyStatsException;
import com.cylo.response.RequestStatus;
import com.cylo.response.SimpleResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author d.kabha
 * 
 */

@Service
public class WordFrequencyService extends AsyncServices  {

	private static Map<String, Integer> wordFrequency = new HashMap<>();
	private ObjectMapper objectMapper = new ObjectMapper();
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WordFrequencyService.class);


	/**
	 * @param words
	 */
	@Async("asyncTaskExecutor")
	public CompletableFuture<SimpleResponse> saveWords(String words, String jobId) {

		LOG.info("Received request with job-id {} and list of words {}", jobId, words);
		CompletableFuture<SimpleResponse> task = new CompletableFuture<SimpleResponse>();

		String[] wordList = words.split(",");
		for (String word : wordList) {
			wordFrequency.merge(word.trim(), 1, Integer::sum);
		}

		task.complete(new SimpleResponse(jobId, RequestStatus.COMPLETE, new WordFrequencyStats(null, 0, 0)));
		LOG.info("Completed processing the request.");
		return task;

	}

	/**
	 * @return WordFrequencyStats
	 * @throws WordFrequencyStatsException
	 */

	public WordFrequencyStats getWordFrequencyStatsByJobId(String jobId) throws Throwable {
		LOG.info("getWordFrequencyStats: retrieve the words and thier statistics from datastore");

		CompletableFuture<SimpleResponse> completableFuture = fetchJob(jobId);

		if (null == completableFuture) {
			List<Integer> frequencies = new ArrayList<>(wordFrequency.values());
			Collections.sort(frequencies);
			int minFrequency = getMinFrequency(frequencies);
			int medianFrequency = getMedianFrequency(frequencies);
			List<WordFrequency> topFrequencies = getTopFrequencies(wordFrequency);
			WordFrequencyStats output = new WordFrequencyStats(topFrequencies, minFrequency, medianFrequency);
			if (null != output) {
				return output;
			}

			throw new ErrorWhileProcessingRequest(JOB_WITH_SUPPLIED_JOB_ID_NOT_FOUND, true);
		}

		if (!completableFuture.isDone()) {
			throw new ErrorWhileProcessingRequest("Job is still in progress...", true);
		}

		Throwable[] errors = new Throwable[1];
		SimpleResponse[] simpleResponses = new SimpleResponse[1];
		completableFuture.whenComplete((response, ex) -> {
			if (ex != null) {
				errors[0] = ex.getCause();
			} else {
				simpleResponses[0] = response;
			}
		});

		if (errors[0] != null) {
			throw errors[0];
		}

		return simpleResponses[0].getWordFrequencyStats();

	}

	/**
	 * @param frequencies
	 * @return minFrequency
	 */
	private int getMinFrequency(List<Integer> frequencies) {
		int minFrequency = frequencies.isEmpty() ? 0 : frequencies.get(0);
		return minFrequency;
	}

	/**
	 * @param frequencies
	 * @return MedianFrequency
	 */
	private int getMedianFrequency(List<Integer> frequencies) {
		LOG.info("getMedianFrequency: calculate the median frequency and retrieve it");
		int size = frequencies.size();
		if (size == 0) {
			return 0;
		}
		if (size % 2 == 0) {
			int middle = size / 2;
			return (frequencies.get(middle - 1) + frequencies.get(middle)) / 2;
		} else {
			return frequencies.get(size / 2);
		}
	}

	/**
	 * @param wordFrequency
	 * @return List<WordFrequency>
	 */
	private List<WordFrequency> getTopFrequencies(Map<String, Integer> wordFrequency) {
		LOG.info("getTopFrequencies: get the 5th top words with their frequencies ");
		return wordFrequency.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.limit(5).map(e -> new WordFrequency(e.getKey(), e.getValue())).collect(Collectors.toList());
	}

	/**
	 * @param words
	 * @return Map< Json key, Json Value>
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public Map<String, String> readJsonInputValue(String words) throws JsonMappingException, JsonProcessingException {
		LOG.info("readJsonInputValue: explict and get the json cvalue from the request ");

		// read the JSON request into a Map
		Map<String, String> requestMap = null;
		requestMap = objectMapper.readValue(words, new TypeReference<Map<String, String>>() {
		});
		return requestMap;
	}

	/**
	 * @param jobId
	 * @return Status of the request 
	 * @throws Throwable
	 */
	public SimpleResponse getJobStatus(String jobId) throws Throwable {
		CompletableFuture<SimpleResponse> completableFuture = fetchJobElseThrowException(jobId);

		if (!completableFuture.isDone()) {
			return new SimpleResponse(jobId, RequestStatus.IN_PROGRESS);
		}

		Throwable[] errors = new Throwable[1];
		SimpleResponse[] simpleResponses = new SimpleResponse[1];
		completableFuture.whenComplete((response, ex) -> {
			if (ex != null) {
				errors[0] = ex.getCause();
			} else {
				simpleResponses[0] = response;
			}
		});

		if (errors[0] != null) {
			throw errors[0];
		}

		return simpleResponses[0];
	}

	/**
	 * @return all WordFrequencyStats
	 * @throws WordFrequencyStatsException 
	 */
	public WordFrequencyStats getWordFrequencyStatsAll() throws WordFrequencyStatsException {
		LOG.info("getWordFrequencyStats: retrieve the words and thier statistics from datastore");
		if (wordFrequency.isEmpty()) {
			throw new WordFrequencyStatsException("the data store is empty , please insert some words before");
		}
		List<Integer> frequencies = new ArrayList<>(wordFrequency.values());
		Collections.sort(frequencies);
		int minFrequency = getMinFrequency(frequencies);
		int medianFrequency = getMedianFrequency(frequencies);
		List<WordFrequency> topFrequencies = getTopFrequencies(wordFrequency);
		return new WordFrequencyStats(topFrequencies, minFrequency, medianFrequency);
	}

}
