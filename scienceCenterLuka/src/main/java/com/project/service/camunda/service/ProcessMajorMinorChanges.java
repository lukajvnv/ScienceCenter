package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.project.dto.ArticleProcessDto;
import com.project.model.OpinionAboutArticle;

public class ProcessMajorMinorChanges implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		OpinionAboutArticle authorsMessage = (OpinionAboutArticle) execution.getVariable("authorMessage");
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		
		authorsMessage.setIteration(authorsMessage.getIteration() + 1);
		
		articleProcessDto.getAuthorsMessages().add(authorsMessage);
		articleProcessDto.setIteration(articleProcessDto.getIteration() + 1);
		
		execution.setVariable("articleProcessDto", articleProcessDto);

	}

}
