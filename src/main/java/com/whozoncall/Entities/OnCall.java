package com.whozoncall.Entities;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.whozoncall.Constants.IntegrationTypes;
import com.whozoncall.Tasks.PDOnCallsFetchWorker;

@Entity
@Table(name="oncall")
public class OnCall {
  
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  
  private String onCallScheduleName;
  
  // this could be one of this App's USP
  private String onCallScheduleNameAlias;
  
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "integration_id")
  @Nullable
  @JsonIgnore
  private List<Integration> integration;
  
  
  private String scheduleId;
  
  private String curOncallUserId;
  
  private String nextOncallUserId;
  
  
  // only when type is PAGERDUTY, will add for subsequent types
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pd_account_id",nullable = true)
  private PDAccount pdaccount;
  
  
  private  String curOnCallStartTime;
  
  private IntegrationTypes type;
  
  // Current User's End time is as same as 
  // next user's Start time i.e : 2021-01-16T02:00:00
  private  String nextOnCallStartTime;
  
  private  String nextOnCallEndTime;
  
  
  
  // this will help in scheduling next topic post
  private  long nextOnCallStartTimeEpoc;
	
  public OnCall(){}
  
  //constructor
  public OnCall(String scheduleId, String name, IntegrationTypes type, PDAccount acc)
  {
	  this.scheduleId = scheduleId;
	  
	  this.onCallScheduleName = name;
	  
	  this.type = type;
	  
	  this.pdaccount = acc;
  }
  
  
		public long getNextOnCallStartTimeEpoc() {
		return nextOnCallStartTimeEpoc;
	}
	
	public void setNextOnCallStartTimeEpoc(long nextOnCallStartTimeEpoc) {
		this.nextOnCallStartTimeEpoc = nextOnCallStartTimeEpoc;
	}
	
	public String getScheduleId() {
		return scheduleId;
	}
	
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	public String getCurOncallUserId() {
		return curOncallUserId;
	}
	
	public void setCurOncallUserId(String curOncallUserId) {
		this.curOncallUserId = curOncallUserId;
	}
	
	public String getNextOncallUserId() {
		return nextOncallUserId;
	}
	
	public void setNextOncallUserId(String nextOncallUserId) {
		this.nextOncallUserId = nextOncallUserId;
	}
	
	public String getNextOnCallStartTime() {
		return nextOnCallStartTime;
	}
	
	public void setNextOnCallStartTime(String nextOnCallStartTime) {
		this.nextOnCallStartTime = nextOnCallStartTime;
	}

	public String getNextOnCallEndTime() {
		return nextOnCallEndTime;
	}

	public void setNextOnCallEndTime(String nextOnCallEndTime) {
		this.nextOnCallEndTime = nextOnCallEndTime;
	}

	public String getCurOnCallStartTime() {
		return curOnCallStartTime;
	}

	public void setCurOnCallStartTime(String curOnCallStartTime) {
		this.curOnCallStartTime = curOnCallStartTime;
	}

	public String getOnCallScheduleName() {
		return onCallScheduleName;
	}

	public void setOnCallScheduleName(String onCallScheduleName) {
		this.onCallScheduleName = onCallScheduleName;
	}

	public IntegrationTypes getType() {
		return type;
	}

	public void setType(IntegrationTypes type) {
		this.type = type;
	}

	public String getOnCallScheduleNameAlias() {
		return onCallScheduleNameAlias;
	}

	public void setOnCallScheduleNameAlias(String onCallScheduleNameAlias) {
		this.onCallScheduleNameAlias = onCallScheduleNameAlias;
	}
	  
	  
  
}