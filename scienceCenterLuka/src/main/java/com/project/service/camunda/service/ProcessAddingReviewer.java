package com.project.service.camunda.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.repository.UnityOfWork;

@Service
public class ProcessAddingReviewer implements JavaDelegate {
	
	@Autowired
	private UnityOfWork unityOfWork;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		
//		List<EditorReviewerByScienceAreaDto> reviewers = (List<EditorReviewerByScienceAreaDto>) execution.getVariable("reviewers");
//		List<String> reviewersIds = reviewers.stream().map(c -> c.getEditorReviewer().getUserUsername()).collect(Collectors.toList());

		List<Long> scAreasId = new ArrayList<Long>();
		String multiIds = (String) execution.getVariable("reviewer_beginMulti");
		Arrays.asList(multiIds.split(":")).forEach(scId -> {
			scAreasId.add(Long.parseLong(scId));
		}); ;
		
		List<String> reviewersIds = unityOfWork.getEditorReviewerByScienceAreaRepository()
			.findAllById(scAreasId)
			.stream().map(rev -> rev.getEditorReviewer().getUserUsername()).collect(Collectors.toList());
		
		articleProcessDto.setReviewers(reviewersIds);
		
		execution.setVariable("articleProcessDto", articleProcessDto);
		
		
		
		//List<String> reviewersList = articleProcessDto.getReviewers();

		
	}

}
