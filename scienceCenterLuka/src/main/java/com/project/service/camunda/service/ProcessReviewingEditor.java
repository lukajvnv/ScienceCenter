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
import com.project.dto.ReviewingEditorDto;
import com.project.model.OpinionAboutArticle;

@Service
public class ProcessReviewingEditor implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		
		String processInstanceId = execution.getProcessInstanceId();
		String subProcessInstanceId = execution.getParentId();
		
		ReviewingEditorDto requestDto = (ReviewingEditorDto) execution.getVariable("editorsReviewing");

		
		
		
		articleProcessDto.getEditorOpinions().add(requestDto.getEditorOpinion());
		execution.setVariable("articleProcessDto", articleProcessDto);

	}

}
