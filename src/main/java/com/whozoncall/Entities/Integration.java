package com.whozoncall.Entities;

import java.util.Date;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.whozoncall.Constants.IntegrationTypes;

@Entity
@Table(name="integration")
public class Integration{

	
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  
  
  private Integer nextInvocation;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "account_id")
  private Account account;
  
  private Date createdOn;
  
  //e.g. this is a list can have Schedules of PD and/ or OnCalls of Opsgenie can be clubbed.
  @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy="integration")
  private List<OnCall> onCall;
  
  
  private IntegrationTypes toType;
  
  //e.g. to channel #UKJ64776 on slack
  private String toTypeId;
  
  private String toTypeName; //#devops channel
  
  
  // picked from slack User tz
  private Boolean updateWithZone = true ;
  
  /* actual message to be computed T-5 minutes before the Scheduled Post   
  *  (in case next on call is < 5 minutes away from now, do it now)
  *  also limited to 250 chars for SLACK
  */
  private String topicString;
  
  private String topicString_250_chars;
  
  private Date modifiedOn;
  
  private Boolean active = true;


public String getToTypeId() {
	return toTypeId;
}

public void setToTypeId(String toTypeId) {
	this.toTypeId = toTypeId;
}



public String getTopicString() {
	return topicString;
}

public void setTopicString(String topicString) {
	this.topicString = topicString;
}

	public Account getAccount() {
	return account;
}

public void setAccount(Account account) {
	this.account = account;
}


public IntegrationTypes getToType() {
	return toType;
}

public void setToType(IntegrationTypes toType) {
	this.toType = toType;
}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}
	
	
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@PrePersist
	public void setCreatedOnDefault() {
		this.createdOn = new Date();
	}
	
	public Date getModifiedOn() {
		return modifiedOn;
	}
	
	
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	@PreUpdate
	public void setModifiedOnDefault()
	{
		this.modifiedOn = new Date();
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getUpdateWithZone() {
		return updateWithZone;
	}

	public void setUpdateWithZone(Boolean updateWithZone) {
		this.updateWithZone = updateWithZone;
	}

	public List<OnCall> getOnCall() {
		return this.onCall;
	}

	public void setOnCall(List<OnCall> OnCall) {
		this.onCall = OnCall;
	}

	/*T.B.D
	 * 
	 * Invoked after every successful topic update to fetch subsequent on call 
	 */
	public void updateIntegration()
	{
		
	}

	public Integer getNextInvocation() {
		return nextInvocation;
	}

	public void setNextInvocation(Integer nextInvocation) {
		this.nextInvocation = nextInvocation;
	}

	public String getTopicString_250_chars() {
		return topicString_250_chars;
	}

	public void setTopicString_250_chars(String topicString_250_chars) {
		this.topicString_250_chars = topicString_250_chars;
	}

	public String getToTypeName() {
		return toTypeName;
	}

	public void setToTypeName(String toTypeName) {
		this.toTypeName = toTypeName;
	}
	
	
	  
}