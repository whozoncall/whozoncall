package com.whozoncall.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.whozoncall.Dao.PDAccountRepository;
import com.whozoncall.Entities.PDAccount;

import javax.servlet.http.HttpServletResponse;


@Controller	
@RequestMapping(path="/v1/account") 
public class AccountHandler {

	private static final Logger log = LoggerFactory.getLogger(AccountHandler.class);
	
	@Autowired
	PDAccountRepository pdAccountRepo;

	@GetMapping(path="/PD/PKCEflow1") 
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void pKCEflow1(HttpServletResponse response, @RequestParam String error, 
			@RequestParam String error_description,@RequestParam String subdomain,
			@RequestParam String code) {

		/* Default request is READ permissions for PD
		 * code is passed if the Auth goes well
		 */
		
		if(error != null && error_description != null)
		{
			//pdAccount.setError(error);
			//pdAccount.setErrorDescription(error_description);
			log.error(" PD Auth failed, error -"+error+" with description - "+error_description);
			
			// handle retry here
			
			return;
		}
		else
		{
			log.debug(" PD PKCE flow 1 success ");
			
			PDAccount pdAcc = new PDAccount();
			
			//pdAccountRepo
			
		}
		
		
		
	}

}
