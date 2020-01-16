package com.project.dto;

import java.io.Serializable;

import com.project.model.OpinionAboutArticle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewArticleMfDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1929356833837174697L;
	
	private String taskInitiator;
	private String previousTaskInitiator;
	
	private String subProcessExecutionId;
	private ReviewingDto reviewerOpinion;

}
