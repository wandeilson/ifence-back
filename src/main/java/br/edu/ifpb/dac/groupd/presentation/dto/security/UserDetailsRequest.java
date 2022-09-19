package br.edu.ifpb.dac.groupd.presentation.dto.security;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserDetailsRequest {
	@NotNull
	@NotBlank
	@NotEmpty
	private String username;
	
	@NotNull
	@NotBlank
	@NotEmpty
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}