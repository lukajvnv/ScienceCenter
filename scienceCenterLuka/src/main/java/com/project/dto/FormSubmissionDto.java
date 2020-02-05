package com.project.dto;

import java.io.Serializable;
import java.util.List;

public class FormSubmissionDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9031161914394638372L;
	private String fieldId;
	private Object fieldValue;
	private List<Object> fieldMultiValues;

	private String multiple;
	
	private String multiEnum;
	
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

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public List<Object> getFieldMultiValues() {
		return fieldMultiValues;
	}

	public void setFieldMultiValues(List<Object> fieldMultiValues) {
		this.fieldMultiValues = fieldMultiValues;
	}

	public String getMultiEnum() {
		return multiEnum;
	}

	public void setMultiEnum(String multiEnum) {
		this.multiEnum = multiEnum;
	}
	
	
	
}
