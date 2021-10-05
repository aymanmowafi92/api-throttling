package com.atypon.apithrottling.functional;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class CacheEntry {

	CircularFifoQueue<LocalDateTime> requestsTimeStampsCircularQueue;
	AtomicInteger numberOfQueuedRequests;
	
	public CacheEntry(CircularFifoQueue<LocalDateTime> requestsTimeStampsCircularQueue, AtomicInteger numberOfQueuedRequests) {
		this.requestsTimeStampsCircularQueue = requestsTimeStampsCircularQueue;
		this.numberOfQueuedRequests = numberOfQueuedRequests;
	}
	
	public CircularFifoQueue<LocalDateTime> getRequestsTimeStampsCirculaQueue() {
		return this.requestsTimeStampsCircularQueue;
	}
	
	public AtomicInteger getNumberOfQueuedRequests() {
		return this.numberOfQueuedRequests;
	}
}
