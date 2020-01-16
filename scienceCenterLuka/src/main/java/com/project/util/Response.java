package com.project.util;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class Response implements Serializable {
	
	
	
	/**
	 * 
	 */
	private String message;
	private HttpStatus status;
	
	public Response(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

}
