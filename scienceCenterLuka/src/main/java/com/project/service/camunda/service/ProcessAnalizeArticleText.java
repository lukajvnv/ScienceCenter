package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.model.OpinionAboutArticle;
import com.project.model.enums.ArticleStatus;
import com.project.model.enums.ReviewingType;
import com.project.model.enums.RoleOfPersonsOpinion;
import com.project.repository.UnityOfWork;

@Service
public class ProcessAnalizeArticleText implements JavaDelegate {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		boolean textOk = (boolean) execution.getVariable("is_text_ok");
		if(!textOk) {
			String comment = (String) execution.getVariable("analize_article_comment");
			
			ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
			OpinionAboutArticle opinion = new OpinionAboutArticle(
					articleProcessDto.getArticleId(), 
					articleProcessDto.getWhoIsChiefEditor(), 
					ReviewingType.INITIAL, 
					ArticleStatus.IRRELEVANT_BY_TEXT, 
					comment, comment, 1);
			
			articleProcessDto.getOpinions().add(opinion);
			execution.setVariable("articleProcessDto", articleProcessDto);
		}

	}

}
