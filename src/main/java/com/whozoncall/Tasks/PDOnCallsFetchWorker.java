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
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.whozoncall.Constants.APIEndPoints;
import com.whozoncall.Constants.IntegrationTypes;
import com.whozoncall.Dao.PDAccountRepository;
import com.whozoncall.Entities.OnCall;
import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Entities.PDAuthEntity;

/*
 * This a misnomer, i'm only fetching schedules for customers to be able to 1st select it and 
 * include them into at at least one integration pipeline
 * 
 * It makes sense to fetch respective onCall users for active schedules once the integration itself is 
 * active and eventually PAID for... alwa magaaaaa :-D 
 */

@Service
public class PDOnCallsFetchWorker {
	
	
	Logger logger = LoggerFactory.getLogger(PDOnCallsFetchWorker.class);
	
	
	@Autowired
	private PDAccountRepository pDAccountRepo;
	
	
	void populateSchedules(PDAccount acc, CloseableHttpClient client){
		
		
		ObjectMapper mapper = new ObjectMapper();
		   HttpGet request = new HttpGet(APIEndPoints.PD_SCHEDULES_GET.value);

		   request.setHeader("Accept", APIEndPoints.PD_API_CONTENT_TYPE.value);
		   request.setHeader("Content-Type", "application/json");
		   request.setHeader("Authorization", "Bearer "+acc.getAccessToken());
		   
		   Boolean hasMore = false;
		   Integer offSet = 0;
		   ArrayList<OnCall> onCalls = new ArrayList<>();
		   
		   do{
		   
			   logger.error(request.getURI().toString());
			   try (CloseableHttpResponse response = client.execute(request)) {
				   
				   if(response.getStatusLine().getStatusCode() == 200 )
				   {
					   JSONObject RespArr = new JSONObject(EntityUtils.toString(response.getEntity()));
				   
					   
					   if(RespArr.length() > 0 && RespArr.has("schedules"))
					   {
					   		JSONArray schedules = RespArr.getJSONArray("schedules");
					   		
					   		hasMore = RespArr.has("more") ? RespArr.getBoolean("more") : false;
					   		
					   		if(hasMore)
					   		{
					   			offSet = RespArr.has("limit") ? RespArr.getInt("limit")+offSet : offSet;
					   			request.setURI(new URI(APIEndPoints.PD_SCHEDULES_GET.value+"?offset="+offSet));
					   			
					   		}
					   			for(int j=0;j<schedules.length();j++)
					   		{
					   				onCalls.add(
					   						new OnCall(
						   						schedules.getJSONObject(j).getString("id"),  // scheduleId
						   						schedules.getJSONObject(j).getString("name"),// scheduleName
						   						IntegrationTypes.PAGERDUTY,
						   						acc)
					   						);
					   		}
					   		
					   }
					   else
					   {
						   logger.error("error parsing Schdules json response for account ->"+acc.getId());
					   }
					   
				   
				   }
				   else
				   {
					   logger.error("Unable to fetch onCalls for Account ->"+acc.getId()
							   +" , status response -> "+response.getStatusLine().toString());
				   }
			   }

			   catch(Exception e )
				{
				   logger.error("Stack trace -> ", e);
				}
			   
		   }  
		   while(hasMore);
		   
		   
		   if(onCalls.size() > 0) {
			   
			   acc.setOnCalls(onCalls);
			   acc.setOnCallsFetchDone(true);
			   
			   pDAccountRepo.save(acc);
		   }
			   
		   
			   
	}
	
	@Async
	public synchronized void startJob(){
		
		Iterable<PDAccount> pdAccItr = pDAccountRepo.findByAuthDoneTrueAndOnCallsFetchDoneFalse();
		
		Iterator<PDAccount> i = pdAccItr.iterator();
		
		PDAccount acc = null;
		
		
				
		try(CloseableHttpClient client = HttpClients.createDefault()){
			
			
			while(i.hasNext())
			{
			 
			   acc = i.next();
			   
			   populateSchedules(acc,client);
			    
			}
		
			
		}
		catch(Exception e )
		{
			
			logger.error("Stack trace -> ", e);

		}
		
		
		
		
			
	}
	
}
