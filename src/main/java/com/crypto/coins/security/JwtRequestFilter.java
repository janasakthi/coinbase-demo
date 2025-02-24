package com.crypto.coins.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crypto.coins.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;


@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

	public JwtRequestFilter(JwtUtil jwtUtil) {
		this.jwtUtil=jwtUtil;
	}
	
	private JwtUtil jwtUtil;
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
	    String path = request.getRequestURI();
	    return path.startsWith("/index.html") ||
	    	   path.startsWith("/assets/") || 
	           path.startsWith("/favicon.ico") || 
	           path.endsWith(".js") || 
	           path.endsWith(".css") || 
	           path.startsWith("/api/authenticate") ||  // Skip authentication endpoint
	           path.equals("/") ||                      // Skip root
	           path.startsWith("/login") ||              // Skip Angular routes
	           path.startsWith("/history");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		log.info("Called the Fileter");
		// Skip JWT processing for /api/authenticate
	    String path = request.getRequestURI();
	    if (path.startsWith("/login") || path.startsWith("/api")==false || path.startsWith("/api/authenticate")) {
	        chain.doFilter(request, response);
	        return;
	    }
	    log.info("Auth Fiinglter");
		final String authorizationHeader = request.getHeader("Authorization");

		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			username = jwtUtil.extractUsername(jwt);
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtUtil.validateToken(jwt, username)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(

						new User(username, "", new ArrayList<>()), null, new ArrayList<>());

				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			} else {
				// Token is invalid, clear context and send 401
				SecurityContextHolder.clearContext();
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}else if (username == null) { // No token provided, send 401
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		chain.doFilter(request, response);
	}
}
