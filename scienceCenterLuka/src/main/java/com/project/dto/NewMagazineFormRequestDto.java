package com.project.dto;

import java.util.List;

import org.camunda.bpm.engine.form.FormField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMagazineFormRequestDto {
	
	private String taskId;
	private String processInstanceId;
	
	private FormField name;
	private FormField issn_number;
	private FormField payment_option;
	private FormField membership_price;
	private FormField science_area;
	
	private String commentIfExist;

}
