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
import com.whozoncall.Dao.AccountRepository;
import com.whozoncall.Dao.PDAccountRepository;
import com.whozoncall.Dao.UserRepository;
import com.whozoncall.Entities.Account;
import com.whozoncall.Entities.AccountRegistrationEntity;
import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Entities.User;
import com.whozoncall.Tasks.PDAuthFetchWorker;
import com.whozoncall.Tasks.PDOnCallsFetchWorker;
import com.whozoncall.Tasks.PDUsersFetchWorker;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;


@Controller	
@RequestMapping(path="/v1/PD") 
public class PagerDutyAuthHandler {

	private static final Logger log = LoggerFactory.getLogger(PagerDutyAuthHandler.class);
	
	@Autowired
	ObjectFactory<HttpSession> httpSessionFactory;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	UserRepository userRepo;

	@Autowired
	PDAccountRepository pdAccountRepo;
	
	@Autowired
	PDAuthFetchWorker pdAuthFetchWorker;
	
	/*
	 * Users are prompted for Authentication from PagerDuty and 
	 * on successfull Auth, Access token is shared this endpoint 
	 * 
	 * 
	 */
	@GetMapping(path="/handleAuthCode")
	public ResponseEntity<?> Register(@RequestParam String code, @RequestParam String subdomain){
		
			Account acc = accountRepo.findByAccountName(subdomain);
			
			if(acc!= null)
			{
				
				PDAccount pdAccount = new PDAccount();
				pdAccount.setCode(code);
				pdAccount.setSubdomain(subdomain);
				pdAccount.setAccount(acc);
				pdAccountRepo.save(pdAccount);
				pdAuthFetchWorker.startJob();
				
			}
			else
			{
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
			
		return new ResponseEntity<>(null, HttpStatus.OK);
		
		}



}
