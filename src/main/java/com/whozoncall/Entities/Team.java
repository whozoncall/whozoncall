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
"id",
"name"
})
@Embeddable
public class Team {
	
@JsonProperty("id")
private String team_id;



@JsonProperty("id")
public String getTeam_id() {
	return team_id;
}
@JsonProperty("id")
public void setTeam_id(String team_id) {
	this.team_id = team_id;
}

@JsonProperty("name")
private String name;


@JsonProperty("name")
public String getName() {
return name;
}

@JsonProperty("name")
public void setName(String name) {
this.name = name;
}

}