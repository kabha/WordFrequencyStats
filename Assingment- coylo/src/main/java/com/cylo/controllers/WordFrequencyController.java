package com.cylo.controllers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cylo.beans.WordFrequencyStats;
import com.cylo.exceptions.ErrorWhileProcessingRequest;
import com.cylo.exceptions.WordFrequencyStatsException;
import com.cylo.response.RequestStatus;
import com.cylo.response.SimpleResponse;
import com.cylo.services.AsyncJobsManager;
import com.cylo.services.WordFrequencyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


@RestController
@RequestMapping("/api/v1/wordfrequency")
public class WordFrequencyController {
	
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(WordFrequencyController.class);
	@Autowired
	WordFrequencyService wordFreqService;
	
	@Autowired
	AsyncJobsManager asyncJobsManager;

	@PostMapping(path="/addwords"  ,produces = "application/json")
	public SimpleResponse addWords(@RequestBody String words) throws ErrorWhileProcessingRequest, JsonMappingException, JsonProcessingException {
		
		Map<String, String> requestMap = null;
		
		LOG.info("Received request for asynchronous processing ");
		String jobId = UUID.randomUUID().toString();
		LOG.info("Generated job-id {} for this request.", jobId);
		
		if(null != wordFreqService.fetchJob(jobId)) {
			throw new ErrorWhileProcessingRequest("A job with same job-id already exists!", true);
		}
		
		
		requestMap = wordFreqService.readJsonInputValue(words);
		String wordJasonVal = requestMap.get("words");
		
		CompletableFuture<SimpleResponse> completableFuture = wordFreqService.saveWords(wordJasonVal , jobId);

		asyncJobsManager.putJob(jobId, completableFuture);

		LOG.info("Job-id {} submitted for processing. Returning from controller.", jobId);
		return new SimpleResponse(jobId, RequestStatus.SUBMITTED);
		
	
	}

	@GetMapping("/wordFrequencyStats/{job-id}")
	public ResponseEntity<WordFrequencyStats> getWordFrequencyStatsPerJobId(@PathVariable(name = "job-id") String jobId) throws Throwable {

		LOG.info("Received request to fetch WordFrequencyStats of job-id: {}", jobId);
		WordFrequencyStats wordFrequencyStats = wordFreqService.getWordFrequencyStatsByJobId(jobId);
		return ResponseEntity.ok().body(wordFrequencyStats);

	}
	
	@GetMapping("/wordFrequencyStatsAll")
	public ResponseEntity<WordFrequencyStats> getWordFrequencyStatsAll() throws Throwable {

		LOG.info("Received request to fetch all the WordFrequencyStats");
		WordFrequencyStats wordFrequencyStats = wordFreqService.getWordFrequencyStatsAll();
		return ResponseEntity.ok().body(wordFrequencyStats);

	}

}
