package com.whozoncall.Entities;

import java.sql.Date;
import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="user")
public class User{

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  
  private String userName;
  
  private String email;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  @JsonIgnore
  private Account account;
  
  

// encrypted string
  private String password;
  
  private Long createdOn;
  
  private Long modifiedOn;
  
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@PreUpdate
	void updateModifiedOn()
	{
		this.modifiedOn = Instant.now().getEpochSecond();
	}
	
	@PrePersist
	void updateModifiedOnAndCreatedOn()
	{
		this.modifiedOn = this.createdOn =  Instant.now().getEpochSecond();
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
		  
}