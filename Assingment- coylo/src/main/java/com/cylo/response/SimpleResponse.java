package com.cylo.response;

import com.cylo.beans.WordFrequencyStats;

public class SimpleResponse  extends BaseResponse {



	private static final long serialVersionUID = -7438317232897830004L;
	private String jobId;
	private WordFrequencyStats wordFrequencyStats;
	
	public SimpleResponse() {
		super();
	}

	public SimpleResponse(String jobId, RequestStatus requestStatus) {
		super(requestStatus);
		this.jobId = jobId;
	}

	public SimpleResponse(String jobId, RequestStatus requestStatus, WordFrequencyStats wordFrequencyStats) {
		super(requestStatus);
		this.wordFrequencyStats = wordFrequencyStats;
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public WordFrequencyStats getWordFrequencyStats() {
		return wordFrequencyStats;
	}

	public void setWordFrequencyStats(WordFrequencyStats wordFrequencyStats) {
		this.wordFrequencyStats = wordFrequencyStats;
	}

	
	@Override
	public String toString() {
		return "SimpleResponse [jobId=" + jobId + ", wordFrequencyStats=" + wordFrequencyStats + "]";
	}
	
}
