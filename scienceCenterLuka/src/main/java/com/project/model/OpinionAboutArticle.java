package com.project.model;

import java.io.Serializable;

import com.project.model.enums.ArticleStatus;
import com.project.model.enums.ReviewingType;
import com.project.model.enums.RoleOfPersonsOpinion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpinionAboutArticle implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long articleId;
	
	private String personOpinionId;
	// private RoleOfPersonsOpinion personsOpinionRole;
	private ReviewingType reviewingType;
	
	private ArticleStatus opinion;
	
	private String comment;
	private String commentOnlyForEditor;
	
	private int iteration;
}
