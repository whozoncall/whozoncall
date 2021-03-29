package com.whozoncall.Tasks;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.ListenableFuture;
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
	
	private ExecutorService integrationProcessor;
	
	@Autowired
	private SlackAccountRepository slackAccountRepo;
	
	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private IntegrationRepository integrationRepo;
	
	@Autowired
	private TaskRepository taskRepo;
	
	AsyncHttpClient client;
	
	ArrayList<TaskResult> results;
	
	HashMap<String, HashMap<String,String>> slackAccountChannelCodeMap;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmm")
            .withZone(ZoneId.from(ZoneOffset.UTC));
	

	
	
	private void startProcessForMinute(Integer timeInteger) throws IOException{
		
		
		// fetch all Integrations for only timeInteger Minute
		List<Integration> integrations = integrationRepo.findAllPaidAndActiveTrialIntegrationsToSlackForNextInvocation(timeInteger);
		client = AsyncHttpCallWrapper.getAsyncClientInstance();
		
		
		for(Integration integration : integrations)
		{	
		
		client.preparePost(APIEndPoints.SLACK_API_CONVERSATION_TOPIC_SET_POST.value)
			         .setHeader("Content-Type", "application/x-www-form-urlencoded")
			         .setHeader("Authorization", "Bearer "+slackAccountChannelCodeMap.get(integration.getAccount().getId().toString()).get(integration.getToTypeId()))
			         .setBody("channel="+integration.getToTypeId()+"&topic="+integration.getTopicString())
			         .execute(new AsyncHandler<Integer>() {
						private Integer status;
						@Override
						public State onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
							status = responseStatus.getStatusCode();
							
							TaskResult res = new TaskResult();
							res.setIntegrationId(integration.getId());
							res.setNumberOfTries(1);
							res.setTimeOfRequest(Instant.now().toEpochMilli());
							res.setResultStatusCode(status);
							results.add(res);
							return State.ABORT;
						}
						
						@Override
						public void onThrowable(Throwable t) {
							
							TaskResult res = new TaskResult();
							res.setIntegrationId(integration.getId());
							res.setNumberOfTries(1);
							res.setTimeOfRequest(Instant.now().toEpochMilli());
							res.setResultStatusCode(status);
							res.setErrorString(t.getMessage());
							results.add(res);
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
							return 0;
						}
					});
		
		}
		
		for(Integration integration : integrations)
		{
			// updates next on call and the topic string as well
			integration.updateIntegration();
		}
	}
	
	
	
	public void close() throws IOException {
		
		taskRepo.saveAll(results);
		
		client.close();
		
		integrationProcessor.shutdown();
		
	}

	// Let's run this every minute
	@Scheduled(cron="* * * * * *")
	@Async
	public void runEveryMin() throws Exception {
		
		this.startProcessForMinute(Integer.parseInt(formatter.format(Instant.now()).toString()));
			
	}
	
}
