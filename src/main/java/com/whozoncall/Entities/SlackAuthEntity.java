package com.whozoncall.Entities;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.whozoncall.Entities.Team;

@Entity
@Table(name="slack_auth_cred")

public class SlackAuthEntity {

@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private Long id; 

@OneToOne(mappedBy = "slackAuthEntity")
@JoinColumn(name = "slack_channel_account_id",referencedColumnName = "id")
private SlackChannelAccount slackChannelAccount;

@JsonProperty("ok")
private String ok;


@JsonProperty("app_id")
private String app_id;

@JsonProperty("error")
private String error; 

@JsonProperty("authed_user")
@Embedded
private Authed_user authed_user;

@JsonProperty("scope")
private String scope;
@JsonProperty("token_type")
private String token_type;
@JsonProperty("access_token")
private String access_token;
@JsonProperty("bot_user_id")
private String bot_user_id;

@JsonProperty("team")
@Embedded
private Team team;

@JsonProperty("enterprise")
private String enterprise;
@JsonProperty("is_enterprise_install")
private String is_enterprise_install;

@JsonProperty("incoming_webhook")
@Embedded
private Incoming_webhook incoming_webhook;


@JsonProperty("app_id")
public String getApp_id() {
return app_id;
}

@JsonProperty("app_id")
public void setApp_id(String app_id) {
this.app_id = app_id;
}

@JsonProperty("authed_user")
public Authed_user getAuthed_user() {
return authed_user;
}

@JsonProperty("authed_user")
public void setAuthed_user(Authed_user authed_user) {
this.authed_user = authed_user;
}

@JsonProperty("scope")
public String getScope() {
return scope;
}

@JsonProperty("scope")
public void setScope(String scope) {
this.scope = scope;
}

@JsonProperty("token_type")
public String getToken_type() {
return token_type;
}

@JsonProperty("token_type")
public void setToken_type(String token_type) {
this.token_type = token_type;
}

@JsonProperty("access_token")
public String getAccess_token() {
return access_token;
}

@JsonProperty("access_token")
public void setAccess_token(String access_token) {
this.access_token = access_token;
}

@JsonProperty("bot_user_id")
public String getBot_user_id() {
return bot_user_id;
}

@JsonProperty("bot_user_id")
public void setBot_user_id(String bot_user_id) {
this.bot_user_id = bot_user_id;
}

@JsonProperty("team")
public Team getTeam() {
return team;
}

@JsonProperty("team")
public void setTeam(Team team) {
this.team = team;
}

@JsonProperty("enterprise")
public String getEnterprise() {
return enterprise;
}

@JsonProperty("enterprise")
public void setEnterprise(String enterprise) {
this.enterprise = enterprise;
}


@JsonProperty("ok")
public String getOk() {
return ok;
}

@JsonProperty("ok")
public void setOk(String ok) {
this.ok = ok;
}


@JsonProperty("is_enterprise_install")
public String getIs_enterprise_install() {
return is_enterprise_install;
}

@JsonProperty("is_enterprise_install")
public void setIs_enterprise_install(String is_enterprise_install) {
this.is_enterprise_install = is_enterprise_install;
}

@JsonProperty("incoming_webhook")
public Incoming_webhook getIncoming_webhook() {
return incoming_webhook;
}

@JsonProperty("incoming_webhook")
public void setIncoming_webhook(Incoming_webhook incoming_webhook) {
this.incoming_webhook = incoming_webhook;
}

public SlackChannelAccount getSlackChannelAccount() {
	return slackChannelAccount;
}

public void setSlackChannelAccount(SlackChannelAccount slackChannelAccount) {
	this.slackChannelAccount = slackChannelAccount;
}





}