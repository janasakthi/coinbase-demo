package com.crypto.coins.dto;

import lombok.Data;

@Data
public class AuthToken {

	public AuthToken(String access_token) {
		this.access_token = access_token;
	}
	private String access_token;
	private String expiry;
}
