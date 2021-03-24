package com.whozoncall.Entities;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Table(name="account")
public class Account{

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  
  private String guid;
  
  private String accountName;
  
  private Long createdOn;
  
  private Long modifiedOn;
  
  
  private Boolean onTrial = true;
  
  
  private Boolean onPaidSub = false;
  
  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy="account")
  private List<Integration> integrations;
  
  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy="account")
  private List<PDAccount> pdAccounts;
  
  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy="account")
  private List<SlackChannelAccount> slackChannelAccounts;
  
  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy="account")
  private List<IntegrationUser> integrationUsers; 
  
  
  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy="account")
  private List<User> users; 

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public Long getCreatedOn() {
		return createdOn;
	}
	
	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}
	
	public Long getModifiedOn() {
		return modifiedOn;
	}
	
	public void setModifiedOn(Long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	public Boolean getonTrial() {
		return onTrial;
	}
	
	public void setonTrial(Boolean onTrial) {
		this.onTrial = onTrial;
	}
	
	public Boolean getonPaidSub() {
		return onPaidSub;
	}
	
	public void setonPaidSub(Boolean onPaidSub) {
		this.onPaidSub = onPaidSub;
	}
	
	@PrePersist
	void updateModifiedOnAndCreatedOn()
	{
		this.setGuid(UUID.randomUUID().toString());
		this.modifiedOn = this.createdOn =  Instant.now().getEpochSecond();
	}

	
	
	@PreUpdate
	void updateModifiedOn()
	{
		this.modifiedOn = Instant.now().getEpochSecond();
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	  
}