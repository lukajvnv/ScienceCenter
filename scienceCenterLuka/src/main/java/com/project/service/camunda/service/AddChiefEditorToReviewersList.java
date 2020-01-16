package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;

@Service
public class AddChiefEditorToReviewersList implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		
		articleProcessDto.getReviewers().add(articleProcessDto.getWhoIsChiefEditor());
		execution.setVariable("articleProcessDto", articleProcessDto);	
	}

}
