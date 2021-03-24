package com.whozoncall;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.whozoncall.Controllers.AuthHandler;

@Component
public class CustomFilter implements Filter {
	
	private static final Logger log = LoggerFactory.getLogger(CustomFilter.class);

    public void doFilter(
      ServletRequest request, 
      ServletResponse response,
      FilterChain chain) {
    	try {
	    		HttpServletRequest req =(HttpServletRequest)request;
	    		HttpServletResponse res =(HttpServletResponse)response;
	    		
    	//CASE 1 allow calls to Login and register UI and backend calls
    	if(req.getServletPath().endsWith("/login.html") || req.getServletPath().endsWith("/Login") || req.getServletPath().endsWith("/Register")
    			|| req.getServletPath().endsWith("/register.html"))
    		chain.doFilter(request, response);
    	
    	//CASE 2 UI call for Js and stuff then just allow
    	else if(req.getServletPath().contains("/ui/")) { 
    	
	    		if(req.getServletPath().endsWith(".svg") || req.getServletPath().endsWith(".js") ||
	    			req.getServletPath().endsWith(".png") || req.getServletPath().endsWith(".css")
	    			|| req.getServletPath().endsWith(".map") || req.getServletPath().endsWith(".woff2")
	    			|| req.getServletPath().endsWith("register.html") || req.getServletPath().endsWith("login.html"))
	    			
	    			chain.doFilter(request, response);
	    //CASE 2.1 UI call with session = ALLOW
	    		else if(req.getSession(false)!=null)
	    				chain.doFilter(request, response);
	    //CASE 2.2 UI call without session = DENY	     
	    			else
	    				res.sendRedirect("/ui/login.html");
    		}	
    	//CASE 3 any NON UI call with session 
    	else if( req.getSession(false)!=null)
    			chain.doFilter(request, response);
    	//CASE 4 any NON UI call withOUT session
    	else 
    		res.sendError(HttpStatus.UNAUTHORIZED.value());
    			 
    				
    	}
    	
    	catch(Exception e)
    	{
    		log.error(e.getMessage()+" -- Error at filter",e);
    	}
    	
    }

}