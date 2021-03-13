package com.whozoncall.Wrappers;



import static org.asynchttpclient.Dsl.*;

import java.io.IOException;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asynchttpclient.*;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.concurrent.Future;


public class AsyncHttpCallWrapper {
	
	
	Logger logger = LogManager.getLogger(AsyncHttpCallWrapper.class);
	
	
	private AsyncHttpCallWrapper() 
	{
		// private constructor
	}
	
	/*
	 * Making this a singleton client as calling asyncHttpClient() always returns a new client
	 * 
	 * 
	 * public static AsyncHttpClient asyncHttpClient() {
		    return new DefaultAsyncHttpClient();
		  }
	 * 
	 */
	
	public static class asyncClient 
	{ 
		private static final AsyncHttpClient INSTANCE = asyncHttpClient(); 
	}
	
	public static AsyncHttpClient getAsyncClientInstance()
	{
		return asyncClient.INSTANCE;
	}
	
}


/*
 *



	
	int count =0;
	ArrayList<ListenableFuture<Integer>> codes = new ArrayList<>();
	ArrayList<String> statusLine = new ArrayList<>();
	String[] URLs = new String[4];

	URLs[0]= "https://whozoncall.com";
	URLs[1]= "https://www.google.com";
	URLs[2]="https://godambi.com";
	URLs[3]="https://www.facebook.com/";


	while(count < 10000)
	{ 
	AsyncHttpClient c =  asyncHttpClient();

		String cur_URL = URLs[count%4];
		int cur_count = count;
		codes.add(c.prepareGet(cur_URL)
				.execute(new AsyncHandler<Integer>() {
					private Integer status;
					@Override
					public State onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
						status = responseStatus.getStatusCode();
						//System.out.println("******************* status code->"+status);
						if(status.equals(200))
							statusLine.add("TTC="+System.currentTimeMillis()+" URL -"+cur_URL+", status="+true+",cur_count="+cur_count);
						else
							statusLine.add("TTC="+System.currentTimeMillis()+" URL -"+cur_URL+", status="+false+",cur_count="+cur_count);

						return State.ABORT;
					}
					@Override
					public State onHeadersReceived(HttpHeaders headers) throws Exception {

						return State.ABORT;
					}
					@Override
					public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {

						return State.ABORT;
					}
					@Override
					public Integer onCompleted() throws Exception {

						return status;
					}
					@Override
					public void onThrowable(Throwable t) {
						statusLine.add("TTC="+System.currentTimeMillis()+" URL -"+cur_URL+", status="+false+",cur_count="+cur_count);
					}
				}));

		count++;
	}

	Thread.sleep(100000);

	System.out.println(" statuses.size() ="+statusLine.size());
	while(true)
	{

		if(statusLine.size()>7000)
		{
			c.close();
			for(String status : statusLine)
				System.out.println(status);

			break;
		}

	}





 
 *
*/