package com.project.dto;

import java.util.List;

import org.camunda.bpm.engine.form.FormField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalizeDto {
	
	private ArticleDto displayArticle;
	private List<FormField> fields;
	private List<FormSubmissionDto> fieldResults;

	private boolean redirect;
}
