package com.whozoncall.Entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.whozoncall.Constants.IntegrationTypes;

/*
 * 
 *  SLack or PagerDuty or Webex User
 * 
 * 
 */

@Entity
@Table(name="integration_user")
public class IntegrationUser{

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  @JsonIgnore	
  private Account account;
  
  @Column(name="created_on")
  private Date createdOn;
  
  @Column(name="account_type")
  private IntegrationTypes type;
  
  
  @Column(name="email_id")
  private String email;
  
  @Column(name="username")
  private String username;
  
  
  // only when IntegrationType is PAGERDUTY, will add for subsequent types
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pd_account_id",nullable = true)
  private PDAccount pdaccount;
  
  
  // this is slack or PagerDuty or  MS Teams specific user ID
  @Column(name="channel_user_id")
  private String channelUserId;
  
  @Nullable
  private String slackUserTimeZone;
  

@Column(name="modified_on")
  private Date modifiedOn;

public IntegrationUser(){}

//constructor
public IntegrationUser(String channelUserId, String name, IntegrationTypes type, String email)
{
	  this.channelUserId = channelUserId; // e.g. PD  ಅಲ್ಲಿ ಮಗಾ
	  
	  this.username = name;
	  
	  this.type = type;
	  
	  this.email = email;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Account getAccount() {
	return account;
}

public void setAccount(Account account) {
	this.account = account;
}

public Date getCreatedOn() {
	return createdOn;
}

public void setCreatedOn(Date createdOn) {
	this.createdOn = createdOn;
}

public IntegrationTypes getType() {
	return type;
}

public void setType(IntegrationTypes type) {
	this.type = type;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getChannelUserId() {
	return channelUserId;
}

public void setChannelUserId(String channelUserId) {
	this.channelUserId = channelUserId;
}

public Date getModifiedOn() {
	return modifiedOn;
}

public void setModifiedOn(Date modifiedOn) {
	this.modifiedOn = modifiedOn;
}

public String getSlackUserTimeZone() {
	return slackUserTimeZone;
}

public void setSlackUserTimeZone(String slackUserTimeZone) {
	this.slackUserTimeZone = slackUserTimeZone;
}

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}
  

		  
}