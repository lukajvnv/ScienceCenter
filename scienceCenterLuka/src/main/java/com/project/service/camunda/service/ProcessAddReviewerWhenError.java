package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.dto.ReviewArticleMfDto;
import com.project.dto.ReviewingDto;

@Service
public class ProcessAddReviewerWhenError implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");


		ReviewArticleMfDto ra = (ReviewArticleMfDto) execution.getVariable("subProcessData");
		ReviewArticleMfDto r = (ReviewArticleMfDto) execution.getVariableLocal("subProcessData");
		
		String irresponsibleReviewerId = r.getTaskInitiator();
		
		articleProcessDto.getReviewers().add(r.getTaskInitiator());
		
		execution.setVariable("articleProcessDto", articleProcessDto);
		execution.setVariable("subProcessData", r);
	}

}
