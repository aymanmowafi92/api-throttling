package com.atypon.apithrottling.filters;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.atypon.apithrottling.handlers.ThrottlingHandler;

public class ThrottlingFilter implements Filter {

	private static final String IP_PARAMETER_NAME = "ip";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
<<<<<<< HEAD
		String ip = request.getParameter(IP_PARAMETER_NAME);
		boolean serviceAvailable = ThrottlingHandler.getInstance().applyThrottlingRule(ip);
		if(serviceAvailable) {
			chain.doFilter(request,response);
		} else {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Unavailable Service. Please try again later" + LocalDateTime.now());
=======
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
						if(numberOfQueuedRequests.getAndIncrement() <= MAXIMUM_NUMBER_OF_ALLOWED_REQUESTS) {
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
>>>>>>> fd1c406edd236829ae92007da9d78e08d64d1d4b
		}
	}

}
