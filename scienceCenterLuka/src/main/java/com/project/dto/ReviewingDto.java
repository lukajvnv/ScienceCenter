package com.project.dto;

import java.io.Serializable;
import java.util.List;

import com.project.model.OpinionAboutArticle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewingDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8111994278994400192L;
	
	private ArticleDto article;
	private MagazineDto magazine;
	private OpinionAboutArticle opinion;
	
	private List<OpinionAboutArticle> authorsMessages;

	
	private boolean insideMf;

}
