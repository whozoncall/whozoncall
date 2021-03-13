package com.whozoncall.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.whozoncall.Constants.SecurityConstants;
import com.whozoncall.Dao.AccountRepository;
import com.whozoncall.Dao.UserRepository;
import com.whozoncall.Entities.Account;
import com.whozoncall.Entities.AccountRegistrationEntity;
import com.whozoncall.Entities.User;

import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpSession;


@Controller	
@RequestMapping(path="/v1") 
public class AuthHandler {

	private static final Logger log = LoggerFactory.getLogger(AuthHandler.class);
	
	@Autowired
	ObjectFactory<HttpSession> httpSessionFactory;
	
	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	UserRepository userRepo;

	@PostMapping(path="/Register")
	public ResponseEntity<?> Register(@RequestBody AccountRegistrationEntity account){
		
		try {

			Account localAccount = new Account();
			localAccount.setAccountName(account.getAccountName());
			
			User user = new User();
			user.setUserName(account.getUserName());
			user.setPassword(Base64.getEncoder().encodeToString(hash(account.getPassword(),SecurityConstants.SALT.value.getBytes())));
			
			accountRepo.save(localAccount);
			user.setAccount(localAccount);
			userRepo.save(user);
		}
		catch (Exception e)
		{
			log.error(e.getMessage()+" -- Unable to Register");			
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	
	
		return  new ResponseEntity<>(null, HttpStatus.ACCEPTED);
		
		}

	@PostMapping(path="/Login")
	@ResponseBody
	public ResponseEntity<?> Login(@RequestBody AccountRegistrationEntity account, HttpSession session){
		
		try {
			
				Account acc = accountRepo.findByAccountName(account.getAccountName());
				
				if(acc==null)
				{
					log.error("No such account name as "+account.getAccountName());			
					return  new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
				}
				
				User user = userRepo.findAccountByUserNameandPassword(acc.getId(), 
									account.getUserName(), Base64.getEncoder().encodeToString(hash(account.getPassword(),SecurityConstants.SALT.value.getBytes())));
				
				if(user != null)
				{
					// set session attribute for future use 
					session.setAttribute("userId", user.getId());
				}
				else
				{
					if(session != null)
						session.invalidate();
					
					return  new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
				}
			
			}
		catch (Exception e)
			{
				log.error(e.getMessage()+" -- Unable to process login");			
				return  new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
		
		
		return  new ResponseEntity<>(null, HttpStatus.OK);
			
		
	}
	
	
	
	private static byte[] hash(String password, byte[] salt)   
	{  
		try {
		    SecretKeyFactory f = SecretKeyFactory.getInstance(SecurityConstants.ALGORITHM.value);
		    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
	
		    return f.generateSecret(spec).getEncoded();
		}
		catch (Exception e)
		{
			log.error(" Error while generating hash, error message --> "+e.getMessage());
		}
		
		return null;

	}


}
