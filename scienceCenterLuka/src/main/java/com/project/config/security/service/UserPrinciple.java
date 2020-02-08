package com.project.config.security.service;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.aspectj.weaver.loadtime.Agent;
import org.camunda.bpm.engine.identity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.model.user.UserSignedUp;

// NE KORISTI!!!!!!!!
public class UserPrinciple implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id,
			    		String email, String password,
			    		Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;

    }

    public static UserPrinciple build(User camundaUser, UserSignedUp dbUser) {
    	
    	List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
    	   	
    	auth.add(new SimpleGrantedAuthority(dbUser.getRole().toString()));
    	
    	return new UserPrinciple(
                dbUser.getUserId(),
                dbUser.getUserUsername(),
                dbUser.getPassword(),
                auth
        );
    	
//		return new UserPrinciple(
//                dbUser.getUserId(),
//                // dbUser.getEmail(),
//                camundaUser.getId(),
//                camundaUser.getPassword(),
//                auth
//        );
    	
    	/*List<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
    	auth.add(new SimpleGrantedAuthority("USER"));*/
    	
     /*   List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());*/

       
    }

    public Long getId() {
        return id;
    }

    

    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }
}