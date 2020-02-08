package com.project.service.camunda.task;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.config.security.service.UserDetailsServiceImpl;

@Service
public class RegisterUserInitialization implements ExecutionListener {

	@Autowired
	private UserDetailsServiceImpl userDetailService;
	
	@Autowired
	private IdentityService identityService;
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		execution.setVariable("user", "guest");

	}

}
