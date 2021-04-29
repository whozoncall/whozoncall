package com.whozoncall.Tasks;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.whozoncall.Constants.APIEndPoints;
import com.whozoncall.Dao.AccountRepository;
import com.whozoncall.Dao.IntegrationRepository;
import com.whozoncall.Dao.SlackAccountRepository;
import com.whozoncall.Dao.TaskRepository;
import com.whozoncall.Entities.Integration;
import com.whozoncall.Entities.SlackChannelAccount;
import com.whozoncall.Entities.TaskResult;
import com.whozoncall.Wrappers.AsyncHttpCallWrapper;

import io.netty.handler.codec.http.HttpHeaders;


/*
 * 
 * Idea is to Club all tasks (slack topics to be updated)
 * 
 * at Minute level - 
 * 
 * e.g. 1609765166000 is Mon Jan 04 2021 18:29:26
 * also 1609765169000 is Mon Jan 04 2021 18:29:29
 * 
 * But both will be scheduled at Jan 04 2021 18:29th Minute, maybe map key 04011229 (MMddHHmm)
 * 
 * 
 */

@Service
public class SlackInitTaskScheduler {
	
	
	Logger logger = LoggerFactory.getLogger(SlackInitTaskScheduler.class);
	
	@Autowired
	private SlackAccountRepository slackAccountRepo;
	
	@Autowired
	private IntegrationRepository integrationRepo;
	
	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private IntegrationUpdateWorker integrationUpdateWorker;
	
	private AsyncHttpClient client = AsyncHttpCallWrapper.getAsyncClientInstance();
	
	private ArrayList<TaskResult> results = null;
	
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmm")
            .withZone(ZoneId.from(ZoneOffset.UTC));
	
	private List<Integration> integrations = null;
	
	private Integer size = 0;
	
	private Integer thisMin = null;
	
	private JSONObject resultObj = null;
	
	private void startProcessForMinute(Integer timeInteger) throws IOException{
			
		results = new ArrayList<TaskResult>();
		integrations = new ArrayList<Integration>();
		size=0;
		
		try {
			SlackChannelAccount channelAccount = null;
			
		
			// fetch all Integrations for only timeInteger Minute
			integrations = integrationRepo.findAllPaidAndActiveTrialIntegrationsToSlackForNextInvocation(timeInteger);
			size = integrations.size();
			
			for(Integration integration : integrations)
			{	
			
				channelAccount = slackAccountRepo.findByChannelId(integration.getToTypeId());
				
				client.preparePost(APIEndPoints.SLACK_API_CONVERSATION_TOPIC_SET_POST.value)
				         .setHeader("Content-Type", "application/x-www-form-urlencoded")
				         .setHeader("Authorization", "Bearer "+channelAccount.getSlackAuthEntity().getAccess_token())
				         .setBody("channel="+integration.getToTypeId()+"&topic="+integration.getTopicString_250_chars())
				         .execute(new AsyncCompletionHandler<Object>() {
				        	    @Override
				        	    public Object onCompleted(Response response) throws Exception {
				        	    	
				        	    	
				        	    	TaskResult res = new TaskResult();
									res.setIntegrationId(integration.getId());
									res.setNumberOfTries(0);
									res.setTimeOfRequest(Instant.now().toEpochMilli());
									res.setResultStatusCode(response.getStatusCode());
									resultObj = new JSONObject(response.getResponseBody());
									
									if(resultObj.has("ok") && resultObj.getBoolean("ok"))
										res.setErrorString(" Published ");
									else
										if(resultObj.has("ok"))
											res.setErrorString(resultObj.getString("error"));
										else
											res.setErrorString(" something went very wrong!");
									
									results.add(res);
									
									if(results.size()==size)
										close();
										
									return response;
				        	    }
				        	});
			
			}
			
		
		}
		catch(Exception e)
		{
			logger.error(" Slack post error -> ",e);
		}
		
	}
	
	
	
	public void close() throws IOException {
		
		try {
			taskRepo.saveAll(results);
			
			// reset counter for next minute
			size=0;
			
			// update integrations for next update
			integrationUpdateWorker.update(this.integrations);
			
			
		}
		catch(Exception e)
		{
			logger.error(" TaskResult Save error -> ",e);
		}
			
		
	}

	/*
	 *  Let's run this every minute
	 *   Initial delay set to 1 minute to update all onCalls that maybe out of sync from current time
	*/
	@Scheduled(cron="0 * * * * *")
	@Async
	public void runEveryMin() throws Exception {
		
		try {
			thisMin = Integer.parseInt(formatter.format(Instant.now()).toString());
			logger.error("Started for Minute ="+thisMin);
			
			this.startProcessForMinute(thisMin);
			
			logger.error("Ended for Minute ="+thisMin);
		}
		catch(Exception e)
		{
			logger.error(" Slack "+thisMin==null ? " - ": thisMin +" Minute Job error -> ",e);
		}
	}
	
}
