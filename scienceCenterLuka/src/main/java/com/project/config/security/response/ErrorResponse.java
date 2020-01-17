package com.project.config.security.response;

/*
 * Pomocna klasa za vracanje greske prilikom pokusaja logovanja 
 * neverifikovanog korisnika
 * 
 * */

public class ErrorResponse {
	
	private String error;
	private String error2;
	private String error3;
	
	

	public ErrorResponse(String error, String error2, String error3) {
		super();
		this.error = error;
		this.error2 = error2;
		this.error3 = error3;
	}

	public ErrorResponse(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	

}
