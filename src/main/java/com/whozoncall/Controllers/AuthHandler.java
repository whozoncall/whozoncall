package com.whozoncall.Controllers;

import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	public ResponseEntity<Object> Login(@RequestBody AccountRegistrationEntity account, HttpSession session){
		
		try {
			
				Account acc = accountRepo.findByAccountName(account.getCompany());
				
				if(acc==null)
				{
					log.error("No such account name as "+account.getCompany());			
					return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				}
				
				User user = userRepo.findAccountByUserNameOrEmailandPassword(acc.getId(), 
									account.getUsername(), Base64.getEncoder().encodeToString(hash(account.getPassword(),SecurityConstants.SALT.value.getBytes())));
				
				if(user != null)
				{	
					// set session attribute for future use 
					session.setAttribute("userId", user.getId());
					session.setAttribute("accountId", acc.getId());
					return  ResponseEntity.ok().build();
				}
				else
				{
					if(session != null)
						session.invalidate();
					
					log.error("No such user name or email as  "+account.getUsername());
					return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				}
			
			}
		catch (Exception e)
			{
				log.error(e.getMessage()+" -- Unable to process login");			
				return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
			
		
	}
	

	
	@PostMapping(path="/Register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> Register(@RequestBody AccountRegistrationEntity account,
			HttpSession session){
		
		try {

			Account localAccount = new Account();
			localAccount.setAccountName(account.getCompany());
			
			User user = new User();
			user.setUserName(account.getUsername());
			user.setPassword(Base64.getEncoder().encodeToString(hash(account.getPassword(),SecurityConstants.SALT.value.getBytes())));
			user.setEmail(account.getEmail());
			
			accountRepo.save(localAccount);
			user.setAccount(localAccount);
			userRepo.save(user);
			
			session.setAttribute("userId", user.getId());
		}
		catch (Exception e)
		{
			log.error(e.getMessage()+" -- Unable to Register");			
			return ResponseEntity.badRequest().build();
		}
	
	
	
		return  ResponseEntity.ok().build();
		
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
