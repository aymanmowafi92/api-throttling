package com.atypon.apithrottling;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.atypon.apithrottling.handlers.ThrottlingHandler;


@SpringBootTest
class ApiThrottlingApplicationTests {

	@Test
	public void testServiceAvailability() throws Exception {
		LinkedList<Boolean> results = new LinkedList<Boolean>();
		for(int count = 1; count <= 10; count++) {
			results.add(ThrottlingHandler.getInstance().applyThrottlingRule("ip1"));//this should be mocked 
		}
		results.forEach(i -> assertEquals(i, Boolean.TRUE));
	}

	@Test
	public void testCaseWhenTheEleventhRequestSentDuringTheFirstMinute() throws Exception {
		LinkedList<Boolean> results = new LinkedList<Boolean>();
		for(int count = 1; count <= 10; count++) {
			results.add(ThrottlingHandler.getInstance().applyThrottlingRule("ip2"));//this should be mocked 
		}
		results.forEach(i -> assertEquals(i, Boolean.TRUE));
		LocalDateTime t1 = LocalDateTime.now();
		Boolean result2 = ThrottlingHandler.getInstance().applyThrottlingRule("ip2");
		assertEquals(result2, Boolean.TRUE);
		LocalDateTime t2 = LocalDateTime.now();
		Duration duration = Duration.between(t1, t2);
		assertTrue(duration.toMillis() >= 2000);
	}
	
	@Test
	public void testCaseWhenTheTwentyOnethhRequestSentDuringTheFirstMinuteAndWithinTheTwoSecondsOfThrottledRequests() throws Exception {
		LinkedList<Boolean> results = new LinkedList<Boolean>();
		for(int count = 1; count <= 10; count++) {
			results.add(ThrottlingHandler.getInstance().applyThrottlingRule("ip3"));//this should be mocked 
		}
		results.forEach(i -> assertEquals(i, Boolean.TRUE));
		
		final ExecutorService executorService = Executors.newFixedThreadPool(10);
		try {
			for(int counter = 1; counter <= 10; counter++) {
				executorService.submit(new Runnable() {
					@Override
					public void run() {
							LocalDateTime t1 = LocalDateTime.now();
							Boolean result2 = ThrottlingHandler.getInstance().applyThrottlingRule("ip3");
							assertEquals(result2, Boolean.TRUE);
							LocalDateTime t2 = LocalDateTime.now();
							Duration duration = Duration.between(t1, t2);
							assertTrue(duration.toMillis() >= 2000);
					}
				});
			}
		} finally {
			executorService.shutdown();
		}
		Thread.sleep(1800);//This is to make sure all threads started their execution and it should be less than 2000 mills (the throttling value)
		Boolean result3 = ThrottlingHandler.getInstance().applyThrottlingRule("ip3");
		assertEquals(result3, Boolean.FALSE);
	}
}
