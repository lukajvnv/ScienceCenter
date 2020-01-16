package com.project.dto;

import java.io.Serializable;

public class FormSubmissionDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9031161914394638372L;
	private String fieldId;
	private String fieldValue;
	
	
	public FormSubmissionDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FormSubmissionDto(String fieldId, String fieldValue) {
		super();
		this.fieldId = fieldId;
		this.fieldValue = fieldValue;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	
}
