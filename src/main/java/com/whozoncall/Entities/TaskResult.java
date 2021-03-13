package com.whozoncall.Entities;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="task_result")
public class TaskResult{

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  
  private Long integrationId;
  
  private Long timeOfRequest;
  
  private Integer resultStatusCode;
  
  private Integer numberOfTries;
  
  private String errorString;
  
  
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIntegrationId() {
		return integrationId;
	}

	public void setIntegrationId(Long integrationId) {
		this.integrationId = integrationId;
	}

	public Long getTimeOfRequest() {
		return timeOfRequest;
	}

	public void setTimeOfRequest(Long timeOfRequest) {
		this.timeOfRequest = timeOfRequest;
	}

	public Integer getNumberOfTries() {
		return numberOfTries;
	}

	public void setNumberOfTries(Integer numberOfTries) {
		this.numberOfTries = numberOfTries;
	}

	public Integer getResultStatusCode() {
		return resultStatusCode;
	}

	public void setResultStatusCode(Integer resultStatusCode) {
		this.resultStatusCode = resultStatusCode;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}
	
	
	  
}