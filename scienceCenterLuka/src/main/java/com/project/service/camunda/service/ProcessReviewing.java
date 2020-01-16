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
public class ProcessReviewing implements JavaDelegate {

	@Autowired
	private RuntimeService runtimeService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		
		String processInstanceId = execution.getProcessInstanceId();
		String subProcessInstanceId = execution.getParentId();
		
		Map<String, Object> m1 = execution.getVariables();
		Map<String, Object> m2 = execution.getVariablesLocal();
		ReviewArticleMfDto ra= (ReviewArticleMfDto) execution.getVariable("subProcessData");
		ReviewArticleMfDto r = (ReviewArticleMfDto) execution.getVariableLocal("subProcessData");
		
		ReviewingDto reviewingDto = ra.getReviewerOpinion();
		OpinionAboutArticle opinion = reviewingDto.getOpinion();
		
		//ovo ovde ne sljaka
//		VariableInstance user = runtimeService.createVariableInstanceQuery()
//                .processInstanceIdIn(processInstanceId)
//                .variableName("subProcessData")
//                .list()
//                .stream()
//                .filter(v -> ((ReviewArticleMfDto) v.getValue()).getSubProcessExecutionId().equals(subProcessInstanceId))
//                .findFirst().orElse(null);
//		
//		if(user == null) {}
//		ReviewingDto reviewingDto = ((ReviewArticleMfDto)user.getValue()).getReviewerOpinion();
//		OpinionAboutArticle opinion = reviewingDto.getOpinion();
		
		articleProcessDto.getOpinions().add(opinion);
		execution.setVariable("articleProcessDto", articleProcessDto);

	}

}
