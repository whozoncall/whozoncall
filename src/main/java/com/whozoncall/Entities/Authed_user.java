package com.whozoncall.Entities;

import javax.annotation.Generated;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id"
})
@Embeddable
public class Authed_user {


@JsonProperty("id")
private String authed_user_id;

@JsonProperty("id")
public String getAuthed_user_id() {
return authed_user_id;
}

@JsonProperty("id")
public void setAuthed_user_id(String authed_user_id) {
this.authed_user_id = authed_user_id;
}

}