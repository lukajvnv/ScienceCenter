package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class PaymentProcessing implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		String status = (String) execution.getVariable("txStatus");
		
		if(!status.equals("SUCCESS")) {
			 throw new BpmnError("PAYMENT_ERROR", "PAYMENT_ERROR");
		}

	}

}
