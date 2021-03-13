package com.whozoncall.Tasks;

import java.util.*;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whozoncall.Constants.APIEndPoints;
import com.whozoncall.Dao.PDAccountRepository;
import com.whozoncall.Dao.SlackAccountRepository;
import com.whozoncall.Dao.SlackAuthRepository;
import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Entities.PDAuthEntity;
import com.whozoncall.Entities.SlackAuthEntity;
import com.whozoncall.Entities.SlackChannelAccount;


@Service
public class SlackAuthFetchWorker {
	
	
	Logger logger = LoggerFactory.getLogger(SlackAuthFetchWorker.class);
	
	@Autowired
	SlackAccountRepository slackAccountRepo;
	
	@Autowired
	private SlackAuthRepository slackAuthRepo;
	
	@Autowired
	private SlackUserFetchWorker slackUserFetchWorker;
	
	
	@Async
	public synchronized void startJob(SlackChannelAccount acc){
		
		
		
		
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			
			
				
		   ObjectMapper mapper = new ObjectMapper();
		   HttpPost request = new HttpPost(APIEndPoints.SLACK_EXG_TOKEN_FOR_CODE_API.value);
		   request.setHeader("Content-Type", APIEndPoints.SLACK_API_CONTENT_TYPE.value);
		   
		   List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		   nvps.add(new BasicNameValuePair("code", acc.getCode()));
		   nvps.add(new BasicNameValuePair("client_id", APIEndPoints.SLACK_CLIENT_ID.value));
		   nvps.add(new BasicNameValuePair("client_secret", APIEndPoints.SLACK_CLIENT_SECRET.value));
		   
		   request.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8)); 
		   
		   
		   
		   SlackAuthEntity slackAuth = client.execute(request, httpResponse ->
		       mapper.readValue(httpResponse.getEntity().getContent(), SlackAuthEntity.class));
		   
		   slackAuth.setSlackChannelAccount(acc);
		   slackAuthRepo.save(slackAuth);
		   
		   acc.setSlackAuthEntity(slackAuth);
		   slackAccountRepo.save(acc);
		   
		   client.close();
			   
		}
		catch(Exception e)
		{
			logger.error(e.getMessage()+" --> Failed to fetch Access token for slack account");
		}
		
		
		slackUserFetchWorker.startJob(acc);
	}
	
}
