package com.whozoncall.Entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="pd_account")
public class PDAccount{

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private Account account;
  
  
  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy="pdaccount")
  private List<OnCall> onCalls; 
  
  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy="pdaccount")
  private List<IntegrationUser> users; 
  
  private String subdomain;
  
  
  private Boolean authDone = false;
  
  private Boolean onCallsFetchDone = false;
  
  private Boolean usersFetchDone = false;
  
  private String code;
  private String accessToken;
  private String error;
  private String errorDescription;
  
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getSubdomain() {
		return subdomain;
	}
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

	public Boolean getAuthDone() {
		return this.authDone;
	}
	public void setAuthDone(Boolean authDone) {
		this.authDone = authDone;
	}
	public Boolean getOnCallsFetchDone() {
		return this.onCallsFetchDone;
	}
	public void setOnCallsFetchDone(Boolean onCallsFetchDone) {
		this.onCallsFetchDone = onCallsFetchDone;
	}
	public List<OnCall> getOnCalls() {
		return onCalls;
	}
	public void setOnCalls(List<OnCall> onCalls) {
		this.onCalls = onCalls;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Boolean getUsersFetchDone() {
		return usersFetchDone;
	}
	public void setUsersFetchDone(Boolean usersFetchDone) {
		this.usersFetchDone = usersFetchDone;
	}
	public List<IntegrationUser> getUsers() {
		return users;
	}
	public void setUsers(List<IntegrationUser> users) {
		this.users = users;
	}
  
}