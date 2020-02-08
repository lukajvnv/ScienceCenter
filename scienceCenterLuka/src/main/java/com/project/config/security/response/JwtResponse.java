package com.project.config.security.response;

import java.util.List;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.identity.Group;

public class JwtResponse {
    private String token;
//    private String type = "Bearer";
//    private String username;
//	private Collection<? extends GrantedAuthority> authorities;
//	private Long user_id;
    private List<String> groups;

//	public JwtResponse(String accessToken, String username, Collection<? extends GrantedAuthority> authorities, Long user_id) {
//		this.token = accessToken;
//		this.username = username;
//		this.authorities = authorities;
//		this.user_id = user_id;
//	}
//	
	public JwtResponse(String accessToken, List<Group> groups) {
		this.token = accessToken;
		this.groups = groups.stream().map(Group::getId).collect(Collectors.toList());
	}
	
	

//    public Long getUser_id() {
//		return user_id;
//	}
//
//
//
//	public void setUser_id(Long user_id) {
//		this.user_id = user_id;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//
//
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return authorities;
//	}
//
//
//
//	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
//		this.authorities = authorities;
//	}



	public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }



	public List<String> getGroups() {
		return groups;
	}



	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

//    public String getTokenType() {
//        return type;
//    }
//
//    public void setTokenType(String tokenType) {
//        this.type = tokenType;
//    }
}