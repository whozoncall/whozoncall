package com.whozoncall.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.whozoncall.Dao.AccountRepository;
import com.whozoncall.Dao.PDAccountRepository;
import com.whozoncall.Dao.UserRepository;
import com.whozoncall.Entities.Account;
import com.whozoncall.Entities.PDAccount;
import com.whozoncall.Tasks.PDAuthFetchWorker;
import io.netty.handler.codec.http.HttpResponse;
import javax.servlet.http.HttpSession;


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
	public RedirectView HandleAuthCode(@RequestParam String code, @RequestParam String subdomain,
			HttpResponse response, RedirectAttributes attributes){
		
			Account acc = accountRepo.findByAccountName(subdomain);
			
			if(acc!= null)
			{	
				
				PDAccount pdAccount = new PDAccount();
				pdAccount.setCode(code);
				pdAccount.setSubdomain(subdomain);
				pdAccount.setAccount(acc);
				pdAccountRepo.save(pdAccount);
				attributes.addFlashAttribute("error", false);
				pdAuthFetchWorker.startJob();
				
				
			}
			else
			{
				log.error(" Account not found, looks like you haven't yet registered or "
						+ "the account name given was different during Sign up !! Got from PD now = > "+subdomain);
				attributes.addFlashAttribute("error", true);
				return  new RedirectView("/ui/add.html");
				
			}
			
			
		return new RedirectView("/ui/add.html");
		
		}



}
