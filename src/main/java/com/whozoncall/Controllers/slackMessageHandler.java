package com.whozoncall.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.fasterxml.jackson.databind.JsonNode;
import javax.servlet.http.HttpServletResponse;


@Controller	
@RequestMapping(path="/v1/slackMessage") 
public class slackMessageHandler {
	

	@PostMapping(path="/push") 
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String push(HttpServletResponse response, @RequestBody JsonNode event) {

	
		return "";
	}

}
