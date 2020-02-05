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
public class NewArticleRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8848348204449356145L;
	
	private String taskId;
	private String processInstanceId;
	
	private List<ScienceAreaDto> articleScienceAreas;
	private List<TermDto> articleTerms;
	private List<UserDto> coAuthors;
	
	private List<FormField> fields;

}
