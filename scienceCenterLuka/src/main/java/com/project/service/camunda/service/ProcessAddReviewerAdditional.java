package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.model.EditorReviewerByScienceArea;
import com.project.repository.UnityOfWork;

@Service
public class ProcessAddReviewerAdditional implements JavaDelegate {
	
	@Autowired
	private UnityOfWork unityOfWork;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		String nextReviewer = (String) execution.getVariable("additional_reviewer");

//		EditorReviewerByScienceArea rev = unityOfWork.getEditorReviewerByScienceAreaRepository().getOne(Long.parseLong(nextReviewerId));
//		String nextReviewer = rev.getEditorReviewer().getUserUsername();
		
		articleProcessDto.getReviewers().add(nextReviewer);
		
		execution.setVariable("articleProcessDto", articleProcessDto);
		
		execution.setVariable("newInitiator", nextReviewer);
	}

}
