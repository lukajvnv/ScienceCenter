package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PaymentErrorHandler implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("error at payment");
		
		com.project.util.Response response = new com.project.util.Response("Error at payment", HttpStatus.CONFLICT);
		execution.setVariable("error_at_payment", response);
	}

}
