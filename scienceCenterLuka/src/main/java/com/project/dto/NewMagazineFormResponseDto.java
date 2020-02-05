package com.project.dto;

import java.io.Serializable;
import java.util.List;

import org.camunda.bpm.engine.form.FormField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMagazineFormResponseDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8119558772558314232L;
	private String taskId;
	private String processInstanceId;
	
	private List<FormSubmissionDto> formFields;
	
//	private String name;
//	private String issn_number;
//	private String payment_option;
//	private Float membership_price;
//	private List<String> science_area;
	
	private Long magazineDbId;
	private boolean update;

}
