package com.crypto.coins.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.crypto.coins.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SecurityConfig {

	private final JwtRequestFilter jwtRequestFilter;
	
	@Value("${app.env}")
	private String env;

	public SecurityConfig(JwtUtil jwtUtil) {
		this.jwtRequestFilter = new JwtRequestFilter(jwtUtil);
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		log.info("Environment: {}",env);
		if (env.equals("PROD")) {
			log.info("Cross Orgins Disabled");
			http.csrf(csrf -> csrf.disable())
					// Allow all static resources and frontend routes
					.authorizeHttpRequests(authorize -> authorize
							.requestMatchers("/", "/index.html", "/login", "/history", "/favicon.ico", "/assets/**",
									"/*/*.js", "/*/*.css")
							.permitAll().requestMatchers("/api/authenticate").authenticated().requestMatchers("/sim/**")
							.permitAll().requestMatchers("/api/v1/**").authenticated())

					.httpBasic(Customizer.withDefaults()) // Enable Basic Authentication
					.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

					// Registering JwtRequestFilter before UsernamePasswordAuthenticationFilter
					.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		} else {
			log.info("Cross Orgins Enabled");
			http.cors().and().csrf(csrf -> csrf.disable())
					// Allow all static resources and frontend routes
					.authorizeHttpRequests(authorize -> authorize
							.requestMatchers("/", "/index.html", "/login", "/history", "/favicon.ico", "/assets/**",
									"/*/*.js", "/*/*.css")
							.permitAll().requestMatchers("/api/authenticate").authenticated().requestMatchers("/sim/**")
							.permitAll().requestMatchers("/api/v1/**").authenticated())

					.httpBasic(Customizer.withDefaults()) // Enable Basic Authentication
					.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

					// Registering JwtRequestFilter before UsernamePasswordAuthenticationFilter
					.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		}
		return http.build();
	}
}
