package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.model.Magazine;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class FindChiefEditor implements JavaDelegate {
	
	@Autowired
	private UnityOfWork unityOfWork;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(articleProcessDto.getMagazineId());
		UserSignedUp chiefEditor = magazine.getChiefEditor();
		
		articleProcessDto.setWhoIsChiefEditor(chiefEditor.getUserUsername());
		
		execution.setVariable("articleProcessDto", articleProcessDto);
	}

}
