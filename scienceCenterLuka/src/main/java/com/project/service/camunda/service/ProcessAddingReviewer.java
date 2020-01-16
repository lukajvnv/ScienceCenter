package com.project.service.camunda.service;

import java.util.List;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.dto.EditorReviewerByScienceAreaDto;
import com.project.dto.UserDto;
import com.project.model.user.UserSignedUp;

@Service
public class ProcessAddingReviewer implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		List<EditorReviewerByScienceAreaDto> reviewers = (List<EditorReviewerByScienceAreaDto>) execution.getVariable("reviewers");
		
//		List<UserDto> onlyReviewers = reviewers.stream().map(EditorReviewerByScienceAreaDto::getEditorReviewer).collect(Collectors.toList());
//		List<String> reviewersIds = onlyReviewers.stream().map(UserDto::getCity).collect(Collectors.toList());
		List<String> reviewersIds = reviewers.stream().map(c -> c.getEditorReviewer().getUserUsername()).collect(Collectors.toList());

		
		articleProcessDto.setReviewers(reviewersIds);
		
		execution.setVariable("articleProcessDto", articleProcessDto);
		
		
		
		//List<String> reviewersList = articleProcessDto.getReviewers();

		
	}

}
