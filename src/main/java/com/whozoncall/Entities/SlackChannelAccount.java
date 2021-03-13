package com.whozoncall.Entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="slack_channel_account")
public class SlackChannelAccount{
	

	/*
	 * 
	 * IMP : This entity doesn't have much channel info, SlackAuthEntity has all
	 * 
	 * 
	 */

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "account_id")
  private Account account;
  private String code;
  
  private Boolean in_active_integration=false;
  

  @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  private SlackAuthEntity slackAuthEntity;
  
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	public SlackAuthEntity getSlackAuthEntity() {
		return slackAuthEntity;
	}
	public void setSlackAuthEntity(SlackAuthEntity slackAuthEntity) {
		this.slackAuthEntity = slackAuthEntity;
	}
	public Boolean getIn_active_integration() {
		return in_active_integration;
	}
	public void setIn_active_integration(Boolean in_active_integration) {
		this.in_active_integration = in_active_integration;
	}
  
  
}