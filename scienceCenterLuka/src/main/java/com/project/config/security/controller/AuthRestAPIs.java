package com.project.config.security.controller;



import javax.validation.Valid;

import org.aspectj.weaver.loadtime.Agent;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.config.security.jwt.JwtProvider;
import com.project.config.security.response.JwtResponse;
import com.project.dto.SignInDto;
import com.project.model.user.UserSignedUp;
import com.project.repository.UserSignedUpRepository;
import com.project.util.Response;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IdentityService identityService;
    
    @Autowired
    UserSignedUpRepository userSignedUpRepository;
 
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;
   
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInDto loginRequest) {
//    	User user = identityService.createUserQuery().userId(loginRequest.getUsername()).singleResult();
    	UserSignedUp userDb = userSignedUpRepository.findByUserUsername(loginRequest.getUsername());
    	
    	if(/*user == null || */userDb == null) {
			// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(new Response("Invalid username or password", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
    	
    	if(!userDb.isActivatedAccount()) {
			// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(new Response("Invalid username or password", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
    	
    	
    	Authentication authentication = null;
		try {
			
			authentication = authenticationManager.authenticate(
			            new UsernamePasswordAuthenticationToken(
			                    loginRequest.getUsername(),
			                    loginRequest.getPassword()
			            )
			    );
		} catch (AuthenticationException e) {
			System.out.println("ne validan");
			// return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			return new ResponseEntity<>(new Response("Invalid username or password", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}

            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Get auth: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            String jwt = jwtProvider.generateJwtToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
         
            
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities(), userDb.getUserId()));

    	
        
       
    }

//    @PostMapping(value = "/signup")
//    public ResponseEntity<Agent> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
//        	
//    		Agent agent = Creator.createAgent(encoder.encode(signUpRequest.getPassword()),
//        			signUpRequest.getEmail(),signUpRequest.getAddress(),signUpRequest.getPhoneNumber());
//        	
//    		agent = agentRepo.save(agent);
//            return new ResponseEntity<Agent>(agent, HttpStatus.CREATED);   
//    }
}