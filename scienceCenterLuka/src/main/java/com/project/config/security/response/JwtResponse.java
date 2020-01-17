package com.project.config.security.response;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
	private Collection<? extends GrantedAuthority> authorities;
	//private Integer idCompany;
	private Long user_id;

	public JwtResponse(String accessToken, String username, Collection<? extends GrantedAuthority> authorities, Long user_id) {
		this.token = accessToken;
		this.username = username;
		this.authorities = authorities;
		//this.idCompany = id;
		this.user_id = user_id;
	}
	
	

    public Long getUser_id() {
		return user_id;
	}



	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}



	/*public Integer getIdCompany() {
		return idCompany;
	}



	public void setIdCompany(Integer idCompany) {
		this.idCompany = idCompany;
	}*/



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}



	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}



	public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }
}