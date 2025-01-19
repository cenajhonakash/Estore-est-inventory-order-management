package com.ace.estore.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(
				req -> req.requestMatchers("/eureka/**", "/v1/inventory/**").permitAll().anyRequest().authenticated())
				.oauth2ResourceServer(
						oauth -> oauth.jwt(token -> token.jwtAuthenticationConverter(new JwtAuthenticationConverter())))
				.build();
	}
}
