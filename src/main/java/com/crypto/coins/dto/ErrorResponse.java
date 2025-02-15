package com.crypto.coins.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {
	
	private String message;
	private LocalDateTime dateTime;
	private int code;
	
	public ErrorResponse() {}
	public ErrorResponse(int code, String message, LocalDateTime time) {
		this.message = message;
		this.code = code;
		this.dateTime = time;
	}

}
