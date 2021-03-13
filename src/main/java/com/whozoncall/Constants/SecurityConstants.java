package com.whozoncall.Constants;

public enum SecurityConstants {
	
	/*
	 * used during OAuth 2.0: Authorization Code Grant Flow
	 * 
	 * https://developer.pagerduty.com/docs/app-integration-development/oauth-2-auth-code-grant
	 * 
	 * 
	 */
	ALGORITHM("PBKDF2WithHmacSHA512"),
	
	SALT("[B@61bbe9ba");
	
	
	public final String value;
	 
    private SecurityConstants(String value) {
        this.value = value;
    }
}
