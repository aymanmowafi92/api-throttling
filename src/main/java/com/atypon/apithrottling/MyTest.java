package com.atypon.apithrottling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyTest {

	public static void main(String[] args) {

		   ExecutorService executorService = Executors.newFixedThreadPool(100);  
		   for(int count = 0; count < 100; count++) {
			   if(count > 20) {
				   try {
					   Thread.sleep(800);
				   } catch (InterruptedException e2) {
					   // TODO Auto-generated catch block
					   e2.printStackTrace();
				   }
			   }
			   executorService.execute(new Runnable() {  
				   
				   @Override  
				   public void run() {  
						/* HttpClient client = new HttpClient */
					   URL oracle;
					try {
						oracle = new URL("http://localhost:8080/api-throttling/service1?throttling");
						URLConnection yc = oracle.openConnection();
				        try(BufferedReader in = new BufferedReader(new InputStreamReader(
				                                    yc.getInputStream()))) {
				        	
				        	String inputLine;
				        	while ((inputLine = in.readLine()) != null) 
				        		System.out.println(inputLine);
				        } catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				        
				   }  
			   });  
		   }
	        executorService.shutdown();  
	}

}
