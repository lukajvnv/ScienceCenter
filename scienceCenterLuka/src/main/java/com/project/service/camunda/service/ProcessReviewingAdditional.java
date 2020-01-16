package com.project.service.camunda.service;

import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.dto.ReviewArticleMfDto;
import com.project.dto.ReviewingDto;
import com.project.model.OpinionAboutArticle;

@Service
public class ProcessReviewingAdditional implements JavaDelegate {

	@Autowired
	private RuntimeService runtimeService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		
		ReviewingDto reviewingDto = (ReviewingDto) execution.getVariable("additionalReviewing");

		OpinionAboutArticle opinion = reviewingDto.getOpinion();
			
		articleProcessDto.getOpinions().add(opinion);
		execution.setVariable("articleProcessDto", articleProcessDto);
	}

}
