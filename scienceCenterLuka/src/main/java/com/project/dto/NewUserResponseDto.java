package com.project.dto;

import java.io.Serializable;
import java.util.List;

public class NewUserResponseDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7746854778470261131L;
	
	
	private List<FormSubmissionDto> formFields;
	private List<String> scienceAreaId;
	
	
	public NewUserResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public NewUserResponseDto(List<FormSubmissionDto> formFields, List<String> scienceAreaId) {
		super();
		this.formFields = formFields;
		this.scienceAreaId = scienceAreaId;
	}



	public List<FormSubmissionDto> getFormFields() {
		return formFields;
	}
	public void setFormFields(List<FormSubmissionDto> formFields) {
		this.formFields = formFields;
	}
	public List<String> getScienceAreaId() {
		return scienceAreaId;
	}
	public void setScienceAreaId(List<String> scienceAreaId) {
		this.scienceAreaId = scienceAreaId;
	}
	
	
}
