package com.crypto.coins.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crypto.coins.dto.AuthToken;
import com.crypto.coins.util.JwtUtil;

@Service
public class AuthService {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public AuthToken authenticate(String userName) {
		return new AuthToken(jwtUtil.generateToken(userName));
	}

}
