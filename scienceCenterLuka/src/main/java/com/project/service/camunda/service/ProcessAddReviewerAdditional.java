package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.project.dto.AddReviewersDto;
import com.project.dto.ArticleProcessDto;
import com.project.dto.ReviewArticleMfDto;
import com.project.dto.ReviewingDto;

public class ProcessAddReviewerAdditional implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		String nextReviewer = (String) execution.getVariable("additional_reviewer");

		articleProcessDto.getReviewers().add(nextReviewer);
		
		execution.setVariable("articleProcessDto", articleProcessDto);
		
	}

}
