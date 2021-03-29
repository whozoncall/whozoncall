package com.whozoncall.Tasks;

import java.io.IOException;
import java.util.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whozoncall.Constants.APIEndPoints;
import com.whozoncall.Dao.IntegrationUserRepository;
import com.whozoncall.Dao.PDAccountRepository;
import com.whozoncall.Dao.SlackMemberRepository;
import com.whozoncall.Entities.IntegrationUser;
import com.whozoncall.Entities.SlackChannelAccount;
import com.whozoncall.Entities.SlackChannelMember;


@Service
public class SlackUserFetchWorker {
	
	
	Logger logger = LoggerFactory.getLogger(SlackUserFetchWorker.class);
	
	
	@Autowired
	private SlackMemberRepository slackMemberRepo;
	
	@Autowired
	private PDAccountRepository pdAccountRepo;
	
	@Autowired
	private IntegrationUserRepository integrationUserRepo; 
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	
	@Async
	public synchronized void startJob(SlackChannelAccount acc){
		
		
		
		
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			
			
				
		   ObjectMapper mapper = new ObjectMapper();
		   mapper.configure(
				   DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		   HttpGet request = new HttpGet(APIEndPoints.SLACK_CHANNEL_MEMBERS_GET.value+"?channel="
		   +acc.getSlackAuthEntity().getIncoming_webhook().getChannel_id());
		   
		   request.setHeader("Content-Type", APIEndPoints.SLACK_API_CONTENT_TYPE.value);
		   request.setHeader("Authorization", "Bearer "+acc.getSlackAuthEntity().getAccess_token());
		   
		   CloseableHttpResponse httpResponse = client.execute(request);
		   
		   JsonNode membersObj = mapper.readTree(httpResponse.getEntity().getContent());
		   
		   
		   JsonNode members = membersObj.get("members");
		   
		   ArrayList<SlackChannelMember> channelMembers = new ArrayList<>(); 
		   SlackChannelMember tmp = null;
		   IntegrationUser integrationUserTmp = null;
		   
		   for(int i=0;i<members.size();i++)
		   {
			   
			   JsonNode user = fetchUserInfo(acc, members.get(i).asText(), client);
			   
			   tmp = new SlackChannelMember();
			   
			   tmp.setUser_id(members.get(i).asText());
			   tmp.setTz(user.get("user").get("tz").asText());
			   tmp.setTz_label(user.get("user").get("tz_label").asText());
			   tmp.setEmail(user.get("user").get("profile").get("email").asText());
			   
			   // populate this map of PDUserId->SlackUserId
			   Optional<IntegrationUser> hasUser = integrationUserRepo.findByEmail(tmp.getEmail());
			   
			   if(hasUser.isPresent())
			   {
				   integrationUserTmp = hasUser.get();
				   integrationUserTmp.setSlackUser(tmp);
				   integrationUserRepo.save(integrationUserTmp);
				   redisTemplate.opsForValue().setIfAbsent(integrationUserTmp.getChannelUserId(), tmp.getUser_id());
				   redisTemplate.opsForValue().setIfAbsent(tmp.getUser_id(),String.valueOf(tmp.getName().length()));
			   }
			   
			   channelMembers.add(tmp);
			   
		   }
 		   
		   
		   slackMemberRepo.saveAll(channelMembers);
		   
		   client.close();
			   
		}
		catch(Exception e)
		{
			logger.error(e.getMessage()+" --> Failed to fetch users for channel "+acc.getSlackAuthEntity().getIncoming_webhook().getChannel_id(),e);
			
		}
		
		
			
	}


	private JsonNode fetchUserInfo(SlackChannelAccount acc, String userId, CloseableHttpClient client ) throws UnsupportedOperationException, IOException {
		

		   ObjectMapper mapper = new ObjectMapper();
		   mapper.configure(
				   DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		   HttpGet request = new HttpGet(APIEndPoints.SLACK_USERS_INFO_GET.value+"?user="+userId);
		   
		   request.setHeader("Content-Type", APIEndPoints.SLACK_API_CONTENT_TYPE.value);
		   request.setHeader("Authorization", "Bearer "+acc.getSlackAuthEntity().getAccess_token());
		   
		   CloseableHttpResponse httpResponse = client.execute(request);
		   
		   JsonNode user = mapper.readTree(httpResponse.getEntity().getContent());
		   
		   
		   return user;
		
	}
	
}
