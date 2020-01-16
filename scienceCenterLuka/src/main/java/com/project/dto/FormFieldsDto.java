package com.project.dto;

import java.util.List;

import org.camunda.bpm.engine.form.FormField;

public class FormFieldsDto {
	
	private String taskId;
	private List<FormField> formFields;
	private String processInstanceId;
	
	private List<ScienceAreaDto> scAreaDataSource;

	public FormFieldsDto(String taskId, String processInstanceId, List<FormField> formFields) {
		super();
		this.taskId = taskId;
		this.formFields = formFields;
		this.processInstanceId = processInstanceId;
	}
	
	public FormFieldsDto(String taskId, String processInstanceId, List<FormField> formFields, List<ScienceAreaDto> scAreaDataSource) {
		this(taskId, processInstanceId, formFields);
		this.scAreaDataSource = scAreaDataSource;
	}

	public FormFieldsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<FormField> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public List<ScienceAreaDto> getScAreaDataSource() {
		return scAreaDataSource;
	}

	public void setScAreaDataSource(List<ScienceAreaDto> scAreaDataSource) {
		this.scAreaDataSource = scAreaDataSource;
	}
	
	
}
