package com.project.service.camunda.task;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.config.security.service.UserDetailsServiceImpl;
import com.project.model.user.UserSignedUp;

@Service
public class NewMagazineInitialization implements ExecutionListener {
	
	@Autowired
	IdentityService identityService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailService;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		//NE RADI OVO!!!
//		String s;
//		try {
//			s = identityService.getCurrentAuthentication().getUserId();
//			execution.setVariable("user", s);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			System.out.println("Greska");
//		}
		
		UserSignedUp loggedUser = userDetailService.getLoggedUser();
		if(loggedUser == null ) {
			
			return;
		}
		execution.setVariable("user", loggedUser.getUserUsername());
		
		//Privremeno
		// execution.setVariable("user", "editorDemo");

	}

}
