package com.cylo.services;

import org.springframework.stereotype.Service;

import com.cylo.response.BaseResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Service
public class AsyncJobsManager {

	private final ConcurrentMap<String, CompletableFuture<? extends BaseResponse>> mapOfJobs;

	public AsyncJobsManager() {
		mapOfJobs = new ConcurrentHashMap<String, CompletableFuture<? extends BaseResponse>>();
	}

	public void putJob(String jobId, CompletableFuture<? extends BaseResponse> theJob) {
		mapOfJobs.put(jobId, theJob);
	}

	public CompletableFuture<? extends BaseResponse> getJob(String jobId) {
		return mapOfJobs.get(jobId);
	}

	public void removeJob(String jobId) {
		mapOfJobs.remove(jobId);
	}
}