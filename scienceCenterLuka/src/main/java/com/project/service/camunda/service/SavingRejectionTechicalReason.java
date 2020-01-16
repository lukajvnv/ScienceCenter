package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.model.Article;
import com.project.model.Magazine;
import com.project.model.enums.ArticleStatus;
import com.project.repository.UnityOfWork;

@Service
public class SavingRejectionTechicalReason implements JavaDelegate {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		
		Article article = unityOfWork.getArticleRepository().getOne(articleProcessDto.getArticleId());
		article.setStatus(ArticleStatus.REJECTED_TECHNICAL_REASON);
		unityOfWork.getArticleRepository().save(article);
	}

}
