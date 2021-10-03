package com.atypon.apithrottling.filters;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import com.atypon.apithrottling.functional.CircularQueue;

@WebFilter(urlPatterns = { "/api-throttling/*"})
public class ThrottlingFilter implements Filter {

	private final ReentrantLock lock = new ReentrantLock();
	private static final int THROTTLING_VALUE_IN_MILLIS = 2000;
	private static final int TIME_IN_MILLIS_LIMIT = 60000;
	private static final int MAXIMUM_NUMBER_OF_ALLOWED_REQUESTS = 10;
	private static final String CRITERIA_FOR_THROTTLING_PARAMETER_NAME = "throttling";

	CircularQueue requestsTimeStampsCircularQueue;
	AtomicInteger numberOfQueuedRequests;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
		requestsTimeStampsCircularQueue = new CircularQueue(MAXIMUM_NUMBER_OF_ALLOWED_REQUESTS);
		numberOfQueuedRequests = new AtomicInteger(0);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(request.getParameter(CRITERIA_FOR_THROTTLING_PARAMETER_NAME) != null) {
			try {
				LocalDateTime oldestRequestTime = null;
				lock.lock();
				try {
					if(requestsTimeStampsCircularQueue.size() == MAXIMUM_NUMBER_OF_ALLOWED_REQUESTS) {
						oldestRequestTime = requestsTimeStampsCircularQueue.deQueue();
					}
					requestsTimeStampsCircularQueue.enQueue(LocalDateTime.now());
				} finally {
					lock.unlock();
				}
				if(oldestRequestTime != null) {
					Duration duration = Duration.between(oldestRequestTime, LocalDateTime.now());
					if(duration.toMillis() <= TIME_IN_MILLIS_LIMIT) {
						if(numberOfQueuedRequests.getAndIncrement() < MAXIMUM_NUMBER_OF_ALLOWED_REQUESTS) {
							Thread.sleep(THROTTLING_VALUE_IN_MILLIS);
							numberOfQueuedRequests.decrementAndGet();
							chain.doFilter(request,response);
							return;
						} else {
							((HttpServletResponse) response).sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Unavailable Service. Please try again later" + LocalDateTime.now());
							numberOfQueuedRequests.decrementAndGet();
							return;
						}
					}
				}
			} catch (Exception e) {
				System.out.print("Error while getting the current time.");
				e.printStackTrace();
			}
		}
		chain.doFilter(request,response);

	}

}
