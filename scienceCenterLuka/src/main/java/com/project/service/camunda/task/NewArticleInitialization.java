package com.project.service.camunda.task;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.config.security.service.UserDetailsServiceImpl;

@Service
public class NewArticleInitialization implements ExecutionListener {

	@Autowired
	private UserDetailsServiceImpl userDetailService;
	
	@Autowired
	private IdentityService identityService;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
//		UserSignedUp loggedUser = userDetailService.getLoggedUser();
//		if(loggedUser == null ) {
//			
//			return;
//		}
//		execution.setVariable("user", loggedUser.getUserUsername());
//		
		String username = "";
		try {
		   username = identityService.getCurrentAuthentication().getUserId(); //ako nema puca exception
		   System.out.println(username);
		} catch (Exception e) {
			 throw new BpmnError("UnexpectedError", "UnexpectedfddError");
		}
		execution.setVariable("user", username);

		// Privremeno...
		// execution.setVariable("user", "lukaAuthor");
	}

}
