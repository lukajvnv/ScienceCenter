package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpStatus;

import com.project.util.ExceptionHandler.Response;

public class ErrorHandler implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		com.project.util.Response response = new com.project.util.Response("Unexcpected exception", HttpStatus.CONFLICT);
		execution.setVariable("error", response);
		System.out.println("Usao");
	}

}
