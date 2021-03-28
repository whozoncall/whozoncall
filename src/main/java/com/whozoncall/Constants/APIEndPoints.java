package com.whozoncall.Constants;

public enum APIEndPoints {
	
	SLACK_APP_ID("A01FCR0AVFB"),
	
	SLACK_CLIENT_ID("1503467881526.1522850369521"),
	
	SLACK_CLIENT_SECRET("0e1fee68e6b354ef41d61e74f099060f"),
	
	SLACK_SIGNING_SECRET("2cebd1e6a568fe117a314e35b3862b32"),
	
	SLACK_API_USERS_GET("https://slack.com/api/users.list"),
	
	SLACK_EXG_TOKEN_FOR_CODE_API("https://slack.com/api/oauth.v2.access"),
	
	SLACK_CHANNEL_MEMBERS_GET("https://slack.com/api/conversations.members"),
	
	SLACK_USERS_INFO_GET("https://slack.com/api/users.info"),
	
	SLACK_API_CONTENT_TYPE("application/x-www-form-urlencoded"),
	
	SLACK_API_CONVERSATION_TOPIC_SET_POST("https://slack.com/api/conversations.setTopic"),
	
	PD_SCHEDULES_GET("https://api.pagerduty.com/schedules"),
	
	PD_ONCALLS_GET("https://api.pagerduty.com/oncalls"),
	
	PD_USERS_GET("https://api.pagerduty.com/users"),
	
	PD_CLIENT_SECRET("d1498cece3ddc50c250912a8d5cd2df369439b58a5075ced8b41ca161e53b14e"),
	
	PD_CLIENT_ID("b27c0ccb003dd80c1ee16e60b2bd464bacd35042d45527db041294efd19a282d"),
	
	PD_AUTH_REDIRECT_URI("https://whozoncall.com"), // /v1/PD/handleAuthCode 
	
	// this is incomplete, needs Code to be added at the end.
	PD_API_AUTH_POST("https://app.pagerduty.com/oauth/token?grant_type=authorization_code&client_id="+APIEndPoints.PD_CLIENT_ID.value
			+ "&client_secret="+APIEndPoints.PD_CLIENT_SECRET.value+"&redirect_uri="+PD_AUTH_REDIRECT_URI.value+"&code="),
	
	PD_API_CONTENT_TYPE("application/vnd.pagerduty+json;version=2");
	
	public final String value;
	
	private APIEndPoints(String value) {
        this.value = value;
    }
}
