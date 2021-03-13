package com.whozoncall.Tasks;

import java.util.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whozoncall.Constants.APIEndPoints;
import com.whozoncall.Dao.PDAccountRepository;
import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Entities.PDAuthEntity;


@Service
public class PDAuthFetchWorker {
	
	
	Logger logger = LoggerFactory.getLogger(PDAuthFetchWorker.class);
	
	
	@Autowired
	private PDAccountRepository pDAccountRepo;
	
	@Autowired
	PDOnCallsFetchWorker pdOnCallsFetchWorker;
	
	@Autowired
	PDUsersFetchWorker  pdUsersFetchWorker;
	
	@Async
	public synchronized void startJob(){
		
		Iterable<PDAccount> pdAccItr = pDAccountRepo.findByAuthDoneFalse();
		
		Iterator<PDAccount> i = pdAccItr.iterator();
		
		PDAccount acc = null;
		
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			
			while(i.hasNext())
			{
			 
			   acc = i.next();
				
			   ObjectMapper mapper = new ObjectMapper();
			   HttpPost request = new HttpPost(APIEndPoints.PD_API_AUTH_POST.value+acc.getCode());
			  
			   
			   PDAuthEntity authResponse = client.execute(request, httpResponse ->
			       mapper.readValue(httpResponse.getEntity().getContent(), PDAuthEntity.class));
			   
			   acc.setToken(authResponse.getAccess_token());
			   acc.setAuthDone(true);
			   acc.setError(authResponse.getError());
			   acc.setErrorDescription(authResponse.getError_description());
			   pDAccountRepo.save(acc);

			   if(authResponse.getError() == null )
			   {
				   pdOnCallsFetchWorker.startJob();
				   pdUsersFetchWorker.startJob();
			   }	   
			   
			}
		
			client.close();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage()+" --> Failed to fetch Access token");
		}
		
		
			
	}
	
}
