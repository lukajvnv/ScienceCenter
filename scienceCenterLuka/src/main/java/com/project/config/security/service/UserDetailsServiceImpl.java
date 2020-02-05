package com.project.config.security.service;




import org.aspectj.weaver.loadtime.Agent;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.model.user.UserSignedUp;
import com.project.repository.UserSignedUpRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserSignedUpRepository userSignedUpRepository;

    @Autowired
    private IdentityService identityService;
    



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

    	User camundaUser = identityService.createUserQuery().userId(username).singleResult();
        
//        if(camundaUser != null) {
//        	UserSignedUp dbUser = userSignedUpRepository.findByUserUsername(username);
//            return UserPrinciple.build(camundaUser, dbUser);      	
//        } else {
//        	return null;
//        }
        
        UserSignedUp dbUser = userSignedUpRepository.findByUserUsername(username);
        return UserPrinciple.build(camundaUser, dbUser);  
       
    }
    
    public UserSignedUp getLoggedUser() {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = "";
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		} else {
		   // username = principal.toString(); //anonimni user
			return null;
		}
		return userSignedUpRepository.findByUserUsername(username);
    }
    
}