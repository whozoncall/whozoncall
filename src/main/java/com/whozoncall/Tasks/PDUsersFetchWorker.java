package com.whozoncall.Tasks;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.whozoncall.Constants.APIEndPoints;
import com.whozoncall.Constants.IntegrationTypes;
import com.whozoncall.Dao.PDAccountRepository;
import com.whozoncall.Dao.SlackMemberRepository;
import com.whozoncall.Entities.IntegrationUser;
import com.whozoncall.Entities.OnCall;
import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Entities.PDAuthEntity;
import com.whozoncall.Entities.SlackChannelMember;

/*
 * This a misnomer, i'm only fetching schedules for customers to be able to 1st select it and 
 * include them into at at least one integration pipeline
 * 
 * It makes sense to fetch respective onCall users for active schedules once the integration itself is 
 * active and eventually PAID for... alwa magaaaaa :-D 
 */

@Service
public class PDUsersFetchWorker {
	
	
	Logger logger = LoggerFactory.getLogger(PDUsersFetchWorker.class);
	
	
	@Autowired
	private PDAccountRepository pDAccountRepo;
	
	@Autowired
	private SlackMemberRepository slackMemberRepo;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	void populateUsers(PDAccount acc, CloseableHttpClient client){
		
		
		ObjectMapper mapper = new ObjectMapper();
		   HttpGet request = new HttpGet(APIEndPoints.PD_USERS_GET.value);

		   request.setHeader("Accept", APIEndPoints.PD_API_CONTENT_TYPE.value);
		   request.setHeader("Content-Type", "application/json");
		   request.setHeader("Authorization", "Bearer "+acc.getAccessToken());
		   
		   Boolean hasMore = false;
		   Integer offSet = 0;
		   ArrayList<IntegrationUser> users = new ArrayList<>();
		   SlackChannelMember member  = null;
		   
		   do{
		   
			   try (CloseableHttpResponse response = client.execute(request)) {
				   
				   if(response.getStatusLine().getStatusCode() == 200 )
				   {
					   JSONObject RespArr = new JSONObject(EntityUtils.toString(response.getEntity()));
				   
					   
					   if(RespArr.length() > 0 && RespArr.has("users"))
					   {
					   		JSONArray usersArr = RespArr.getJSONArray("users");
					   		
					   		hasMore = RespArr.has("more") ? RespArr.getBoolean("more") : false;
					   		
					   		if(hasMore)
					   		{
					   			offSet = RespArr.has("limit") ? RespArr.getInt("limit")+offSet : offSet;
					   			request.setURI(new URI(APIEndPoints.PD_USERS_GET.value+"&offset="+offSet));
					   			
					   		}
					   			for(int j=0;j<usersArr.length();j++)
					   		{
					   				Optional<SlackChannelMember> hasUser = slackMemberRepo
					   						.findByEmail(usersArr.getJSONObject(j).getString("email"));
					   				
					   				if(hasUser.isPresent())
					   				{
					   					member = hasUser.get();
					   					users.add(
						   						new IntegrationUser(
						   							usersArr.getJSONObject(j).getString("id"), 
						   							usersArr.getJSONObject(j).getString("name"),
							   						IntegrationTypes.PAGERDUTY,
							   						usersArr.getJSONObject(j).getString("email"),
							   						acc,
							   						member) // used to map folks magaaa, bekee beku
						   						);
					   					redisTemplate.opsForValue().setIfAbsent(usersArr.getJSONObject(j).getString("id")
					   							, member.getUserId());
					   				}
					   				else
					   				{
					   					users.add(
						   						new IntegrationUser(
						   							usersArr.getJSONObject(j).getString("id"), 
						   							usersArr.getJSONObject(j).getString("name"),
							   						IntegrationTypes.PAGERDUTY,
							   						usersArr.getJSONObject(j).getString("email"),
							   						acc) // used to map folks magaaa, bekee beku
						   						);
					   				}
					   		}
					   		
					   }
					   else
					   {
						   logger.error("error parsing users json response for account ->"+acc.getId());
					   }
					   
				   
				   }
				   else
				   {
					   logger.error("Unable to fetch users for Account ->"+acc.getId()
							   +" , status response -> "+response.getStatusLine().toString());
				   }
			   }

			   catch(Exception e )
				{
					
					logger.error(" ST -> ", e);
				}
			   
		   }  
		   while(hasMore);
		   
		   
		   if(users.size() > 0)
		   {
			   acc.setUsers(users);
			   acc.setUsersFetchDone(true);
			   pDAccountRepo.save(acc);
		   }
		   
			   
		   
			   
	}
	
	@Async
	public synchronized void startJob(){
		
		Iterable<PDAccount> pdAccItr = pDAccountRepo.findByUsersFetchDoneFalse();
		
		Iterator<PDAccount> i = pdAccItr.iterator();
		
		PDAccount acc = null;
		
		
				
		try(CloseableHttpClient client = HttpClients.createDefault()){
			
			
			while(i.hasNext())
			{
			 
			   acc = i.next();
			   
			   populateUsers(acc,client);
			    
			}
		
			
		}
		catch(Exception e )
		{
			logger.error("Stack trace -> ", e);
		}
		
		
		
		
			
	}
	
}
