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
public class ReviewingEditorDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8111994278994400192L;
	
	private ArticleDto article;
	private MagazineDto magazine;
	private List<OpinionAboutArticle> opinions;
	
	private List<OpinionAboutArticle> authorsMessages;

	
	private OpinionAboutArticle editorOpinion;
	
	private List<FormField> fields;
	private List<FormSubmissionDto> fieldResults;

}
