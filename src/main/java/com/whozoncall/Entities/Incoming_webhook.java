package com.whozoncall.Entities;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"channel",
"channel_id",
"configuration_url",
"url"
})
@Embeddable
public class Incoming_webhook {
	

@JsonProperty("channel")
private String channel;
@JsonProperty("channel_id")
private String channel_id;
@JsonProperty("configuration_url")
private String configuration_url;
@JsonProperty("url")
private String url;

@JsonProperty("channel")
public String getChannel() {
return channel;
}

@JsonProperty("channel")
public void setChannel(String channel) {
this.channel = channel;
}

@JsonProperty("channel_id")
public String getChannel_id() {
return channel_id;
}

@JsonProperty("channel_id")
public void setChannel_id(String channel_id) {
this.channel_id = channel_id;
}

@JsonProperty("configuration_url")
public String getConfiguration_url() {
return configuration_url;
}

@JsonProperty("configuration_url")
public void setConfiguration_url(String configuration_url) {
this.configuration_url = configuration_url;
}

@JsonProperty("url")
public String getUrl() {
return url;
}

@JsonProperty("url")
public void setUrl(String url) {
this.url = url;
}

}