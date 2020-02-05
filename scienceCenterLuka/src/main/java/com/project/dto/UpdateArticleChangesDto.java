package com.project.dto;

import java.io.Serializable;
import java.util.List;

import org.camunda.bpm.engine.form.FormField;

import com.project.model.OpinionAboutArticle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleChangesDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 7267623785811514588L;
	
	private NewArticleRequestDto newArticleRequestDto;
	private NewArticleResponseDto newAarticleResponseDto;
	
	private List<OpinionAboutArticle> reviewersOpinion;
	private List<OpinionAboutArticle> editorsOpinion;
	
	private OpinionAboutArticle authorsMessage;
	
	private List<FormField> fields;
	private List<FormSubmissionDto> fieldResults;
}
