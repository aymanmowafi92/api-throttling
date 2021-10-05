package com.atypon.apithrottling.handlers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.atypon.apithrottling.functional.CacheEntry;

public class ThrottlingHandler {

	private final ReentrantLock lock = new ReentrantLock();
	private static final int THROTTLING_VALUE_IN_MILLIS = 2000;
	private static final int TIME_IN_MILLIS_LIMIT = 60000;
	private static final int MAXIMUM_NUMBER_OF_ALLOWED_REQUESTS = 10;
	
	private static ThrottlingHandler instance;
	
	ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<String, CacheEntry>();
	
	public static ThrottlingHandler getInstance(){
		if(instance == null){
			synchronized (ThrottlingHandler.class) {
				if(instance == null){
					instance = new ThrottlingHandler();
				}
			}
		}
		return instance;
	}
	
	/**
	 * this will apply the throttling criteria and return true if the service is considered available, false otherwise
	 * @param ip
	 * @return
	 */
	public boolean applyThrottlingRule(String ip) {
		if(ip != null) {
			try {
				LocalDateTime oldestRequestTime = null;
				lock.lock();
				CacheEntry cacheEntry = cache.get(ip);
				try {
					if(cacheEntry == null) {
						cacheEntry = new CacheEntry(new CircularFifoQueue<LocalDateTime>(MAXIMUM_NUMBER_OF_ALLOWED_REQUESTS), new AtomicInteger(0));
						cacheEntry.getRequestsTimeStampsCirculaQueue().add(LocalDateTime.now());
						cache.put(ip, cacheEntry);
					} else {
						CircularFifoQueue<LocalDateTime> requestsTimeStampsCirculaQueue = cacheEntry.getRequestsTimeStampsCirculaQueue();
						if(requestsTimeStampsCirculaQueue.isAtFullCapacity()) {
							oldestRequestTime = requestsTimeStampsCirculaQueue.iterator().next();
						}
						requestsTimeStampsCirculaQueue.add(LocalDateTime.now());
					}
				} finally {
					lock.unlock();
				}
				
				if(oldestRequestTime != null) {
					Duration duration = Duration.between(oldestRequestTime, LocalDateTime.now());
					if(duration.toMillis() <= TIME_IN_MILLIS_LIMIT) {
						if(cacheEntry.getNumberOfQueuedRequests().get() < MAXIMUM_NUMBER_OF_ALLOWED_REQUESTS) {
							cacheEntry.getNumberOfQueuedRequests().incrementAndGet();
							Thread.sleep(THROTTLING_VALUE_IN_MILLIS);
							cacheEntry.getNumberOfQueuedRequests().decrementAndGet();
							return true;
						} else {
							return false;
						}
					}
				}
			} catch (Exception e) {
				System.out.print("Error while getting the current time.");
				e.printStackTrace();
			}
		}
		return true;
	}

}
