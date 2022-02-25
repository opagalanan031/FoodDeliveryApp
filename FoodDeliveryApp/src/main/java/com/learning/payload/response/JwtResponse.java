package com.learning.payload.response;

import java.util.List;

import lombok.Data;

@Data
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String name;
	private String email;
	private List<String> roles;
	
	public JwtResponse(String accessToken, Long id, String name, String email, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.name = name;
		this.email = email;
		this.roles = roles; 
	}
}
