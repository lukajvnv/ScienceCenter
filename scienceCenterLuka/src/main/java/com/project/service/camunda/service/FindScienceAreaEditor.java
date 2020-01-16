package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.model.Article;
import com.project.model.EditorReviewerByScienceArea;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class FindScienceAreaEditor implements JavaDelegate {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		Article article = unityOfWork.getArticleRepository().getOne(articleProcessDto.getArticleId());
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(articleProcessDto.getMagazineId());
		
		ScienceArea scienceArea = article.getScienceArea();
		EditorReviewerByScienceArea scienceAreaEditor = unityOfWork.getEditorReviewerByScienceAreaRepository().findByMagazineAndScienceAreaAndEditor(magazine, scienceArea, true);
	
		if(scienceAreaEditor == null) {
			articleProcessDto.setWhoIsScienceEditor(articleProcessDto.getWhoIsChiefEditor());
		}else {
			UserSignedUp scAreaEditor = scienceAreaEditor.getEditorReviewer();
			articleProcessDto.setWhoIsScienceEditor(scAreaEditor.getUserUsername());
		}
		
		execution.setVariable("articleProcessDto", articleProcessDto);
	}

}
