package com.project.util;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.project.exception.SignUpVerificationException;

@ControllerAdvice
public class ExceptionHandler {
	
	@org.springframework.web.bind.annotation.ExceptionHandler({ Exception.class})
	public ResponseEntity<Response> validationFailed(Exception ex){
		
		Response response = new Response();
		response.setMessage(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({SignUpVerificationException.class})
	public ResponseEntity<Response> validationFailed(SignUpVerificationException ex){
		
		Response response = new Response();
		response.setMessage(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	
	
	@org.springframework.web.bind.annotation.ExceptionHandler({org.camunda.bpm.engine.impl.pvm.PvmException.class})
	public ResponseEntity<Response> validationFailed( org.camunda.bpm.engine.impl.pvm.PvmException ex){
		
		Response response = new Response();
		response.setMessage(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	
	
	@org.springframework.web.bind.annotation.ExceptionHandler({BpmnError.class})
	public ResponseEntity<Response> validationFailed( BpmnError ex){
		
		Response response = new Response();
		response.setMessage(ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	
	public static class Response {
		private String message;

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		
		
	}
}
