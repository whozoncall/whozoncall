package com.whozoncall.Controllers;

import org.asynchttpclient.util.HttpConstants.ResponseStatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

import com.whozoncall.Dao.AccountRepository;
import com.whozoncall.Dao.PDAccountRepository;
import com.whozoncall.Dao.SlackAccountRepository;
import com.whozoncall.Dao.UserRepository;
import com.whozoncall.Entities.Account;
import com.whozoncall.Entities.AccountRegistrationEntity;
import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Entities.SlackChannelAccount;
import com.whozoncall.Entities.User;
import com.whozoncall.Tasks.PDAuthFetchWorker;
import com.whozoncall.Tasks.PDOnCallsFetchWorker;
import com.whozoncall.Tasks.PDUsersFetchWorker;
import com.whozoncall.Tasks.SlackAuthFetchWorker;
import com.whozoncall.Tasks.SlackUserFetchWorker;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;


@Controller	
@RequestMapping(path="/v1/slack") 
public class SlackAuthHandler {

	private static final Logger log = LoggerFactory.getLogger(SlackAuthHandler.class);
	
	@Autowired
	ObjectFactory<HttpSession> httpSessionFactory;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	UserRepository userRepo;

	@Autowired
	SlackAccountRepository slackAccountRepo;
	
	@Autowired
	SlackAuthFetchWorker slackAuthFetchWorker;
	
	@Autowired
	SlackUserFetchWorker slackUserFetchWorker;
	/*
	 * Users are prompted for Authentication from Slack and 
	 * on successfull Auth, Access token is shared this endpoint 
	 * 
	 * 
	 */
	@GetMapping(path="/handleAuthCode")
	public RedirectView Register(@RequestParam String code, @RequestParam String state){
		
			Account acc = accountRepo.findByGuid(state);
			
			if(acc!= null)
			{
				
				SlackChannelAccount slackChannelAccount = new SlackChannelAccount();
				slackChannelAccount.setAccount(acc);
				slackChannelAccount.setCode(code);
				slackAccountRepo.save(slackChannelAccount);
				
				log.info(" saved slack channel code for acc with name - "+ acc.getAccountName());
				
				slackAuthFetchWorker.startJob(slackChannelAccount);
				
			}
			else
			{
				log.error(" Looks like you haven't yet created an Account with us !");
				return new RedirectView("/ui/add.html");
			}
			
			return new RedirectView("/ui/add.html");
		
		}
	
	@GetMapping(path="/fetchUsers")
	public ResponseEntity<?> fetchUsers(@RequestParam Long id){
		
			Optional<SlackChannelAccount> acc = slackAccountRepo.findById(id);
			
			slackUserFetchWorker.startJob(acc.get());
			
			
		return new ResponseEntity<>(" All is well! ", HttpStatus.OK);
		
		}
	
	



}
